package com.mangoplay.yeezymusic.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.objects.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AuthenticationActiviity extends AppCompatActivity {
    public static User user;
    boolean loggedIn = false;
    List<String> bannedEmails = new ArrayList<>();

    public GoogleSignInClient mGoogleSignInClient;
    public final static int RC_SIGN_IN = 1;
    Button signInButton;
    public FirebaseAuth mAuth;


    String clientId = "602627867735-27uhiv6gqavbstiouh29ejens54mu6h9.apps.googleusercontent.com";


    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private static final int REQ_ONE_TAP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        System.out.println("onCreate auth");
        setContentView(R.layout.activity_authentication);

        System.out.println("onStart authentication");

        new Thread(this::readFirebaseStorage).start();

        try {
            user = User.readUser(getApplicationContext());
            if(user == null) System.out.println("user is null");
            else {
                loggedIn = true;
                System.out.println("user is not null");
                System.out.println("email: " + user.getEmail());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                if(checkIfEmailIsOkay(user.getEmail())) {
                    startActivity(intent);
                    finish();
                    System.out.println("finished");
                } else{
//                    Toast toast=Toast.makeText(getApplicationContext(),"We are sorry, but this user has been banned from using our services.", Toast.LENGTH_LONG);
                    showUserIsBannedMessage();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(!loggedIn) oneTap();

    }

    private void readFirebaseStorage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("bannedUsersEmails.txt");
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println("success uri: " + uri);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, uri.toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String[] emails = response.split("\n");
                                for(String email : emails){
                                    System.out.println("banned email: " + email);
                                    bannedEmails.add(email.trim());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("failed");
                e.printStackTrace();
            }
        });
    }

    void oneTap(){
        oneTapClient = Identity.getSignInClient(this);
//        System.out.println("clientId: " + R.string.default_web_client_id);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(clientId)
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();

        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                    null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            System.out.println("couldn t start");
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("failed");
                        User user = new User();
                        try {
                            user.saveUser(getApplicationContext());
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
//                        createRequest();
//                        signIn();
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                    }
                });

    }


    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("request code: " + requestCode);

        if (requestCode == 1) {
            System.out.println("shouldn t be here");
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                System.out.println("result code: " + resultCode);
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    user = new User(account.getEmail());
//                    user.saveUser(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    if(checkIfEmailIsOkay(user.getEmail()) == true) {
                    startActivity(intent);
                    finish();
                    System.out.println("finished");
//                    } else {
//                        Toast toast=Toast.makeText(getApplicationContext(),"We are sorry, but this user has been banned from using our services.", Toast.LENGTH_LONG);
//                        showUserIsBannedMessage();
//                    }
                } catch (ApiException e) {
                    e.printStackTrace();
//                    createRequest();
//                    signIn();
//                    Toast toast=Toast.makeText(getApplicationContext(),"You need to be signed in to use this app.", Toast.LENGTH_LONG);
//                    toast.show();
                    User user = new User();
                    try {
                        user.saveUser(getApplicationContext());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    System.out.println("finished and failed");
                }
            }
        } else if (requestCode == 2) {
            switch (requestCode) {
                case REQ_ONE_TAP:
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                        String idToken = credential.getGoogleIdToken();
                        String username = credential.getId();
                        String password = credential.getPassword();
                        user = new User(username);
                        user.saveUser(getApplicationContext());
                        uploadUserToDatabase(user);
                        logLoginSuccessfull();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        if(checkIfEmailIsOkay(user.getEmail())) {
                            startActivity(intent);
                            finish();
                            System.out.println("finished");
                        } else {
//                            Toast toast=Toast.makeText(getApplicationContext(),"We are sorry, but this user has been banned from using our services.", Toast.LENGTH_LONG);
                            showUserIsBannedMessage();
                        }
                    } catch (ApiException e) {
                        logLoginFailed();
                        e.printStackTrace();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        // ...
                    } catch (IOException e) {
                        e.printStackTrace();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
            }
        }
    }

    private void uploadUserToDatabase(User user) {
        System.out.println("uploading to database");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        System.out.println("got database instance");
        Map<String, Object> map = new HashMap<>();
        map.put("email", user.getEmail());
        db.collection("users").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("user uploaded to the database succesfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("user upload to the database failed");
                System.out.println(e.toString());
            }
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("auth succesful");
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            if(checkIfEmailIsOkay(user.getEmail())) {
                                startActivity(intent);
                                finish();
                                System.out.println("finished");
                            } else {
//                                Toast toast=Toast.makeText(getApplicationContext(),"We are sorry, but this user has been banned from using our services.", Toast.LENGTH_LONG);
                                showUserIsBannedMessage();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            System.out.println("Auth failed");
                        }
                    }
                });
    }

    boolean checkIfEmailIsOkay(String email){
        System.out.println("email to check if banned: " + email);
        System.out.println("banned emails size: " + bannedEmails.size());
        for(String bannedEmail : bannedEmails){
            if(bannedEmail.equals(email.trim())) {
                System.out.println("user " + email + " is banned");
                return false;
            }
        }
        return true;
    }

    void showUserIsBannedMessage(){
        System.out.println("showing user is banned message");
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog))
                .setTitle("User is banned")
                .setMessage("We are sorry, but this user has been banned from using our services.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Quit app", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAndRemoveTask();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    void logLoginSuccessfull(){
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "user_logged_in");
        mFirebaseAnalytics.logEvent("user_logged_in", bundle);
    }

    void logLoginFailed(){
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "user_log_in_failed");
        mFirebaseAnalytics.logEvent("user_log_in_failed", bundle);
    }
}

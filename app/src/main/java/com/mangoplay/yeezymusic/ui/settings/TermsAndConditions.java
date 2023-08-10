package com.mangoplay.yeezymusic.ui.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mangoplay.yeezymusic.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import androidx.appcompat.app.AppCompatActivity;

public class TermsAndConditions extends AppCompatActivity {

    TextView text;
    ImageView backButtonTop;
    Button backButtonBottom;
    String url = "https://mangoplayservices.blogspot.com/2021/08/these-terms-and-conditions-of-use-for.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        text = findViewById(R.id.text);
//        text.setText(termsAndConditions);
        backButtonTop = findViewById(R.id.back_button_top);
        backButtonBottom = findViewById(R.id.back_button_bottom);

        backButtonBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("back pressed");
                finish();
            }
        });

        backButtonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("back pressed");
                finish();
            }
        });

        new Thread(() -> {
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
                Element ps = doc.select("#ppBody").first();
                if(ps != null) {
                    String result = br2nl(ps.toString());
                    String parse = Jsoup.parse(result).text();
                    String string = parse.replace("\\n", "\n");
                    System.out.println("ps: ");
                    System.out.println(string);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(string);
                        }
                    });
                } else throw new IOException();
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String string = "Sorry can't load the Terms and Conditions right now.Please try again.";
                        text.setText(string);
                    }
                });
            }
        }).start();

    }

    public String br2nl(String html) {
        if(html==null)
            return html;
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        document.select("br").append("\\n");
        document.select("article").append("\\n");
        document.select("p").prepend("\\n\\n");
        String s = document.html();
        return Jsoup.clean(s, "", Safelist.none(), new Document.OutputSettings().prettyPrint(false));
    }


    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private String termsAndConditions = "These terms and conditions of use for https://esound.app (\"Website\") and the software application eSound Music (\"Application\", \"the app\", \"the application\", \"the Services\"), available on the iOS App Store, Google Play Store and Huawei App Gallery, constitute a legal agreement and are entered into by and between you (\"the user\") and eSound Music. (\"we\", \"us\", \"our\").\n" +
            "\n" +
            "The following terms and conditions, together with any documents and/or additional terms they expressly incorporate by reference (collectively, \"Terms and Conditions\"), govern your access to and use of, the Website and the Application (collectively, \"Services\", \"Service\"), including any content and functionality offered on, or through, the Services.\n" +
            "\n" +
            "BY USING THE SERVICES, OR BY CLICKING TO ACCEPT THE TERMS AND CONDITIONS, YOU ACCEPT AND AGREE TO BE BOUND AND COMPLY WITH THESE TERMS AND CONDITIONS AND OUR PRIVACY POLICY, INCORPORATED HEREIN BY REFERENCE.\n" +
            "\n" +
            "IF YOU DO NOT AGREE TO THESE TERMS AND CONDITIONS OR THE PRIVACY POLICY, YOU MUST NOT ACCESS OR USE THE SERVICES.\n" +
            "\n" +
            "By using the Services, you represent and warrant that you are the legal age of majority under applicable law to form a binding contract with us, and that you meet all of the foregoing eligibility requirements. If you do not meet all of these requirements, you must not access or use the Services.\n" +
            "\n" +
            "We reserve the right in our sole discretion to revise and update the Terms and Conditions from time to time.\n" +
            "\n" +
            "All such modifications are effective immediately upon posting and apply to all access to and continued use of the Services.\n" +
            "\n" +
            "You agree to periodically review the Terms and Conditions to be aware of any such modifications and your continued use of the Services after such modifications shall constitute your acceptance of them.\n" +
            "\n" +
            "The information and material on the Services, and the functionality of the Services, may be changed, withdrawn or terminated at any time in our sole discretion, without notice. We will not be liable if, for any reason, all or any part of the Services is restricted to users or unavailable at any time or for any period. You agree that we have no responsibility or liability for the deletion of, or the failure to store or to transmit, any information maintained by us.\n" +
            "\n" +
            "We reserve the right, as reasonably necessary or convenient, either for our own purposes or to improve the quality of any of the components comprising the Services, to change rules of operation for the technologies therein comprised, system interfaces, utilities, operating and other systems and software, and to implement enhancements, amendments or updates thereto. To minimize the effect of service outages, we will undertake reasonable efforts to schedule times during which the Services will be unavailable to you, due to maintenance.\n" +
            "\n" +
            "Important\n" +
            "eSound Music is a cross-platform media center born to simplify the management, presentation, and playback of YouTube videos (or other free third-party platforms contents) by using a simple user-friendly interface.\n" +
            "\n" +
            "eSound Music does not contain, in its servers or architectures, any copyrighted content or media.\n" +
            "\n" +
            "The app does not and never will independently distribute any copyrighted song or media content through its Services.\n" +
            "\n" +
            "The user is obliged to view and access only content for which he or she has viewing rights.\n" +
            "\n" +
            "The user may only stream video contents from YouTube platform (or other third-party platforms) that are not limited by YouTube (or other third-party platforms, i.e. region-restricted videos) or for which he or she holds the rights of viewing and use (according to YouTube Terms &amp; Conditions, or the Terms &amp; Conditions of other third-party services which the user adopted inside the app).\n" +
            "\n" +
            "The user accepts to use eSound Music for its own personal, non-commercial purposes only.\n" +
            "\n" +
            "We do not endorse or support the stream of copyrighted material without the permission of the respective owners.\n" +
            "\n" +
            "Any misuse of the app to illegally view content that would otherwise be paid for is not approved by eSound Music and any copyright infringement is solely attributable to the end user.\n" +
            "\n" +
            "Users who do not respect the Terms and Conditions may see their account terminated and be prohibited from accessing eSound Music.\n" +
            "\n" +
            "Copyright\n" +
            "\n" +
            "All images, content, logos and trademarks are the property of their respective owners.\n" +
            "\n" +
            "You understand and agree that the Services and the entire contents, features and functionality that comprise the Services, including, but not limited to, all information, software, code, text, displays, graphics, photographs, video, audio, design, presentation, selection and arrangement, are owned by us, our licensors, or other providers of such material and are protected in all forms by intellectual property laws, including without limitation, copyright, trade-mark, patent, trade secret and any other proprietary rights.\n" +
            "\n" +
            "You may only use the Services for your personal and non-commercial use, and only for lawful purposes and in accordance with these Terms and Conditions. You shall not directly or indirectly reproduce, compile for an internal database, distribute, modify, create derivative works of, publicly display, publicly perform, republish, download, store, or transmit any of the material on the Services, in any form or medium whatsoever, except that:\n" +
            "(a) your device and browser may temporarily store or cache copies of materials being accessed and viewed;\n" +
            "(b) a reasonable number of copies for personal use only may be printed, keeping any proprietary notices thereon, which may only be used for non-commercial and lawful personal use and not for further reproduction, publication, or distribution of any kind on any medium whatsoever;\n" +
            "(c) one single user copy may be downloaded with any proprietary notices intact, for your own personal, non-commercial use, conditional on your agreement to be bound by these Terms and Conditions, in respect of your use of such downloads; and\n" +
            "(d) if social media platforms are linked to certain content on, or in, the Services, you may take such actions as the Services and such third-party social media platforms permit.\n" +
            "\n" +
            "Users are not permitted to modify copies of any materials from the Services, nor to delete or alter any copyright, trade-mark, or other proprietary rights notices from copies of materials from the Services. You must not access or use for any commercial purposes any part of the Services or any services or materials available through the Services.\n" +
            "\n" +
            "If you print, copy or download any part of the Services in breach of these Terms and Conditions, your right to use the Services will cease immediately and you must, at our option, return or destroy any copies of the materials you have made. You have no right, title, or interest in or to the Services or to any content on, or in, the Services, and all rights not expressly granted are reserved by us. Any use of the Services that is not expressly permitted by these Terms and Conditions is a breach of these Terms and Conditions and may infringe or violate copyright, trade-mark, and other intellectual property or other proprietary laws.\n" +
            "\n" +
            "We reserve the non-expiring right to use and disclose (for commercial purposes or otherwise) the textual and numerical statistical portion of all data in the Services at any time, to conduct analysis for industry trends.\n" +
            "eSound Music is a viewer (browser) of video contents from YouTube (or other third-party platforms) and does not store nor hold any content in its infrastructure.\n" +
            "The app simply creates a user friendly interface between the user and the YouTube platform (or other third-party platforms).\n" +
            "\n" +
            "eSound Music is not responsible for the content displayed since all of it comes from the YouTube platform (or other third-party platforms).\n" +
            "\n" +
            "In any case we respect the intellectual property rights of our artists and content providers.\n" +
            "If you are a copyright holder who believes that any content available on the app infringes upon your rights, please let us know.\n" +
            "\n" +
            "Notice of alleged copyright infringement should be sent to eSound Music’s designated copyright agent at the following address: copyright@esound.app\n" +
            "\n" +
            "A notification of claimed copyright infringement must be addressed to eSound Music’s copyright agent listed above and substantially include the following:\n" +
            "\n" +
            "    A physical or electronic signature of the owner (or person authorized to act on behalf of the owner) of the copyright that is allegedly infringed upon;\n" +
            "    Specific identification of each copyrighted work claimed to have been infringed upon;\n" +
            "    An in-app screenshot or a proof of the material claimed to be infringed upon;\n" +
            "    Contact information for the complaining party, such as a complete name, address, telephone number, and email address;\n" +
            "    A statement that the complaining party has a good faith belief that use of the work(s) in the manner complained of is not authorized by the copyright owner, its agent, or the law;\n" +
            "    A statement that the information in the notification is accurate, and under penalty of perjury, that the complaining party is authorized to act on behalf of the owner of an exclusive right that is allegedly infringed upon.\n" +
            "\n" +
            "eSound Music may, in accordance with our own policy, terminate the accounts of users who are found to be repeat infringers of copyright.\n" +
            "Since we believe it is important to protect copyright we also offer a service to protect the works of content creators and copyright owners.\n" +
            "\n" +
            "The service we offer is described below:\n" +
            "\n" +
            "If the owners of copyrighted material want to ensure that their videos are not shown on our platform, they simply have to send an email to copyright@esound.app specifying the YouTube channels for which they have the rights and which they do not want to be shown in our service.\n" +
            "In case the request is legitimate, we will take care as soon as possible to remove and not show such content on our service.\n" +
            "The content, in this case, will remain available only on the YouTube platform (or other third-party platforms).\n" +
            "\n" +
            "Conditions of Use and User Submissions and Site Content Standards\n" +
            "You warrant and agree that your use of the Services shall not:\n" +
            "(a) In any manner violate any applicable federal, provincial, local, or international law or regulation including, without limitation, any laws regarding the export of data or software, patent, trade-mark, trade secret, copyright, or other intellectual property, legal rights (including the rights of publicity and privacy of others) or contain any material that could give rise to any civil or criminal liability under applicable laws or regulations or that otherwise may be in conflict with these Terms and Conditions and our Privacy Policy.\n" +
            "(b) In any manner violate the terms of use of any third-party website that is linked to the Services, including but not limited to, any third-party social media website.\n" +
            "(c) Include or contain any material that is determined to be exploitive, obscene, harmful, threatening, abusive, harassing, hateful, defamatory, sexually explicit or pornographic, violent, inflammatory, or discriminatory, based on race, sex, religion, nationality, disability, sexual orientation, or age or any other such legally prohibited ground, or be otherwise objectionable, such determination to be made in our sole discretion.\n" +
            "(d) Involve stalking, attempting to exploit or harm any individual (including minors) in any way, by exposing them to inappropriate content or otherwise, or ask for personal information as prohibited under applicable laws, regulations or codes.\n" +
            "(e) Involve, provide or contribute any false, inaccurate or misleading information.\n" +
            "(f) Impersonate or attempt to impersonate us, our employees, another user, or any other person or entity (including, without limitation, by using email addresses, or screen names associated with any of the foregoing).\n" +
            "(g) Transmit, or procure the sending of, any advertisements or promotions, sales, or encourage any other commercial activities, including, without limitation, any \"spam\", \"junk mail\", \"chain letter\", contests, sweepstakes and other sales promotions, barter, or advertising or any other similar solicitation.\n" +
            "(h) Fraudulently represent online advertisement impressions, clicks, conversion or data events, in order to generate revenue, or for any other purpose.\n" +
            "(i) Encourage any other conduct that restricts or inhibits anyone\\'s use or enjoyment of the Services, or which, as determined by us, may harm us or users of the Services, or expose them to liability.\n" +
            "(j) Cause annoyance, inconvenience, or needless anxiety, or be likely to upset, embarrass, or alarm any other person.\n" +
            "(k) Promote any illegal activity, or advocate, promote, or assist any unlawful act.\n" +
            "(l) Give the impression that anything you do or say is endorsed by us or any other person or entity, if this is not the case.\n" +
            "\n" +
            "Monitoring and Enforcement, Suspension and Termination\n" +
            "\n" +
            "We have the right, without provision of notice to:\n" +
            "\n" +
            "    Take appropriate legal action, including, without limitation, referral to law enforcement or any regulatory authority, or notifying the harmed party of any illegal or unauthorized use of the Services. Without limiting the foregoing, we have the right to fully cooperate with any law enforcement authorities or court order requesting or directing us to disclose the identity or other information of any user of the Services.\n" +
            "    Terminate, restrict or suspend your access to all or part of the Services, for any or no reason, including, without limitation, any violation of these Terms and Conditions.\n" +
            "\n" +
            "\n" +
            "YOU WAIVE AND HOLD HARMLESS eSound. AND ITS SUBSIDIARIES, AFFILIATES, AND EACH OF THEIR RESPECTIVE DIRECTORS, OFFICERS, EMPLOYEES, AGENTS, SERVICE PROVIDERS, CONTRACTORS, LICENSORS, LICENSEES, SUPPLIERS, SUCCESSORS AND ASSIGNS, FROM ANY AND ALL CLAIMS RESULTING FROM ANY ACTION TAKEN (OR NOT TAKEN) BY eSound AND ANY OF THE FOREGOING PARTIES, RELATING TO ANY INVESTIGATIONS BY EITHER eSound OR SUCH PARTIES, OR BY LAW ENFORCEMENT AUTHORITIES.\n" +
            "We have no obligation, nor any responsibility to any party, to monitor the Services or their use. We have no liability for any action or inaction regarding transmissions, communications, or content provided by any user or third-party, subject to applicable laws.\n" +
            "\n" +
            "Service status and service interruption\n" +
            "eSound Music is committed to providing the most reliable service possible, but reserves the right at any time to terminate it without notice for technical or organizational reasons.\n" +
            "eSound Music is not responsible in case of malfunction of third party services to which the app connects.\n" +
            "eSound Music is not responsible for the accuracy of the data displayed.\n" +
            "eSound Music is not responsible for the termination of certain functions.\n" +
            "eSound Music is not responsible for any loss of data such as \"playlist\", \"videos\" and all settings regarding the app.\n" +
            "You agree to have access to the \"as is\" service, knowing that functionality may be added or removed at any time.\n" +
            "\n" +
            "Third-Party Websites\n" +
            "For your convenience, the Services may provide links or pointers to third-party sites. We make no representations about any other websites that may be accessed from the Services. If you choose to access any such sites, you do so at your own risk. We have no control over the contents of any such third-party sites and accept no responsibility for such sites or for any loss or damage that may arise from your use of them. You are subject to any terms and conditions of such third-party sites.\n" +
            "\n" +
            "Such links to third-party sites from the Services may include links to certain social media features that enable you to link or transmit on your own, or using certain third-party websites, certain content from the Services. You may only use these features when they are provided by us and solely with respect to the content identified by us.\n" +
            "\n" +
            "Consent to Electronic Communication\n" +
            "When you visit the Website or use the Services, or send email or text messages to us, you are communicating with us electronically. You consent to receive communications from us electronically. We will communicate with you by email, text messages or by posting notices on, or through, the Services. You agree that all agreements, notices, disclosures and other communications that we provide to you electronically, satisfy any legal requirement that such communications be in writing and/or signed.\n" +
            "\n" +
            "Terms of Use and Sale for the eSound Premium Service\n" +
            "Article 1 – General\n" +
            "\n" +
            "Welcome to the Terms of Use and Sale for the eSound Premium Service, which includes the website accessible at https://esound.app (hereinafter the “Site”), and its desktop, tablet and mobile applications (hereinafter the “Application”).\n" +
            "\n" +
            "These conditions of use and sale (hereinafter the “Terms”) govern exclusively the eSound Premium Service. These conditions apply to the exclusion of any other terms, particularly those that apply to other Services – free or paid – offered on the Site or the Application.\n" +
            "\n" +
            "The purpose of these Terms is to define the contractual and commercial relationship between eSound, on the one hand, and any subscriber to the eSound Premium Service, eSound Family Service and eSound Student Service (hereinafter the “Subscriber”), on the other hand.\n" +
            "\n" +
            "The use of the Site and of the eSound Premium Service is authorised for personal and private use only, therefore any other use, particularly in public premises and for businesses, is strictly forbidden.\n" +
            "\n" +
            "Subscription and access to the eSound Premium Service are strictly dependent on prior acceptance of all of these Terms (including the Privacy Policy) without restriction or reservation.\n" +
            "Article 2 – Presentation of the eSound Premium Service\n" +
            "\n" +
            "The eSound Premium Service is a service subject to consent to these Terms, to listen to unlimited sound recordings (hereinafter the “Recordings”) without any advertisements or limitations, on multiple devices as described hereunder.\n" +
            "\n" +
            "The eSound Premium Service includes personalised music, playlists and other content recommendations.\n" +
            "\n" +
            "Once the Subscriber has signed up, he/she has access to all features of the eSound Premium Service from the Site or the Application.\n" +
            "\n" +
            "The main characteristics of the eSound Premium Service are as follows:\n" +
            "\n" +
            "- Unlimited access of Recordings included in the eSound Premium Service music catalogue, in order to listen to them later when off-line;\n" +
            "\n" +
            "- No advertising.\n" +
            "\n" +
            "The eSound Premium Service can only be used on all the devices in which the free version is running.\n" +
            "\n" +
            "The eSound Premium Service is accessible to the Subscriber until the end of his/her subscription as stated on the subscription page of the Site at the time of subscribing or the cancellation of his/her subscription under the Terms hereunder.\n" +
            "\n" +
            "The eSound Premium Service is accessible from a personal computer (PC or Mac) by connecting to the Site or desktop application or from a portable device through the Application, which needs to be downloaded by the Subscriber.\n" +
            "\n" +
            "eSound informs the Subscriber that it declines all liability with regard to advertisements displayed within content provided and hosted by third-parties and which may be integrated into and/or referred to on the Site or the Application.\n" +
            "Article 3 – Use of the eSound Premium Service\n" +
            "\n" +
            "The use of the eSound Premium Service requires a high-speed Internet connection and an Internet service for portable devices. These connections are not provided by eSound; the Subscriber must therefore first register with a high-speed Internet and/or mobile Internet offer in order to use the eSound Premium Service.\n" +
            "\n" +
            "A mobile Internet connection via third or fourth generation (3G or 4G) mobile technology is highly recommended.\n" +
            "\n" +
            "The music catalogue available as part of the eSound Premium Service may be subject to change. eSound cannot guarantee the availability of any given track or album or any artist or group in the eSound Premium Service catalogue. Moreover, eSound cannot guarantee that any given track, album, artist or group in the eSound Premium Service catalogue will be available indefinitely (see copyright claims reported above). eSound incurs no liability for the withdrawal of any part of the catalogue content made available.\n" +
            "\n" +
            "As a Subscriber, you can activate the eSound Premium Service on a compatible portable device registered with the eSound Premium Service.\n" +
            "Article 4 – Availability and modification of the eSound Premium Service\n" +
            "\n" +
            "The eSound Premium Service can be accessed 24 hours a day, seven days a week, within the limit of the Terms and the terms of Article 10 hereunder.\n" +
            "\n" +
            "The Recordings made available within the eSound Premium Service are determined based on the country where the Subscriber has subscribed to the eSound Premium Service (his/her country of residence). In accordance with Regulation (EU) 2017/1128 on cross-border portability of online content services in the internal market, any Subscriber who has subscribed to the eSound Premium Service from a member state of the European Union will have access to the same content in the same manner when using the eSound Premium Service while temporarily present in another member state, at no additional cost. However, eSound cannot guarantee the same quality of service as provided in the Subscriber’s country of residence.\n" +
            "\n" +
            "Any Subscriber who has subscribed to the eSound Premium Service outside the European Union will also be given access to the same content in the same manner when using the eSound Premium Service while temporarily present in another country, at no additional cost. However, eSound cannot guarantee the same quality of service as provided in the Subscriber’s country of residence.\n" +
            "\n" +
            "eSound has the right to make any changes or improvements to the eSound Premium Service as it deems necessary. eSound nevertheless guarantees that it will not affect the quality or substantially change the features of the eSound Premium Service.\n" +
            "\n" +
            "In addition, eSound has the right to temporarily remove access to the eSound Premium Service, without prior notice or compensation, where necessary to carry out maintenance or ensure continuity of service. The Subscriber acknowledges that eSound cannot be held liable and thus waives any right to compensation and/or action against eSound in that respect. Notice of temporary interruptions in the eSound Premium Service will be given on the Site at least 24 (twenty-four) hours before they occur, unless they are urgent. eSound is also entitled to shut the Service down permanently with no compensation payable. Notice of the permanent shutdown of the eSound Premium Service will be given via the Site or Application. The Subscriber will no longer be liable for any payment from the date of the eSound Premium Service shutdown.\n" +
            "Article 5 – Absence of the right of withdrawal – period – renewal – cancellation – upgrade to the eSound Premium Service\n" +
            "5.1 No right of withdrawal (cooling-off period)\n" +
            "\n" +
            "The Subscriber expressly recognises and accepts that the supply of the eSound Premium Service starts at the time his/her subscription is confirmed and acknowledges that consequently he/she loses his/her right to withdraw from the subscription. Accordingly, no request for withdrawal, cancellation or refund will be accepted once the subscription is confirmed.\n" +
            "5.2 Period – Trials and discovery offers\n" +
            "\n" +
            "eSound offers a monthly subscription (or any other period depending on the offers made on the Site or the Application).\n" +
            "5.3 Renewal\n" +
            "\n" +
            "Unless cancelled by the Subscriber pursuant to Article 5.4, and unless the Subscriber uses a non-recurring payment method, the subscription to the Service renews automatically for exactly the same duration as the initial period. Therefore, if the Subscriber takes out a monthly subscription, such subscription will be renewed automatically each month.\n" +
            "5.4 Cancellation\n" +
            "\n" +
            "Where a subscription is taken out directly from the Site or Application, in order to cancel his/her subscription the Subscriber must go to his/her device subscription managment (istruction are provided for every platform) and cancel the subscription. Cancellation will be effective at the end of the current subscription period, if made at least 48 (forty-eight) hours before the end of the period. For a trial or discovery period, cancellation occurs at the date and time of cancellation as indicated in the Subscriber’s account, unless stated otherwise on the Site.\n" +
            "\n" +
            "If the subscription was taken out via a third-party website (such as iTunes for example), the Subscriber must follow the instructions to cancel the subscription. The Subscriber should be aware that conditions (how to cancel, within what timeframe, etc.) may vary from one third-party platform to another.\n" +
            "Article 6 – Terms of access to the eSound Premium Service\n" +
            "\n" +
            "The Subscriber declares that he/she has the capacity to agree to these Terms; this means that he/she is of the legal age required and not under any legal protection measure (such as legal guardianship).\n" +
            "\n" +
            "The Subscriber declares that he/she is a resident in a country where the eSound Premium Service is available and that he/she is the owner of a credit card issued by a bank in the same country.\n" +
            "Article 7 – Creation of an account\n" +
            "\n" +
            "In order to subscribe to the eSound Premium Service, every Subscriber must:\n" +
            "\n" +
            "- Create an account on the Application or, if the Subscriber is already registered, log in to the Application;\n" +
            "\n" +
            "- Provide the information requested on the subscription form;\n" +
            "\n" +
            "- Give his/her consent to the Terms;\n" +
            "\n" +
            "- Pay the price for his/her subscription via one of the payment systems offered;\n" +
            "\n" +
            "- Confirm his/her subscription.\n" +
            "\n" +
            "The Subscriber agrees to provide true, accurate and genuine information about himself/herself in connection with his/her registration with the Site, the Application and the eSound Premium Service.\n" +
            "\n" +
            "The Subscriber can change the password given on his/her account at any time by going on the specific section of the website.\n" +
            "\n" +
            "The Subscriber must immediately inform eSound of any loss or unauthorised use of his/her account, identification details or password. The passwords and identification details are personal and the Subscriber undertakes not to disclose them. As a result, the Subscriber is solely responsible for their use.\n" +
            "\n" +
            "The Subscriber is solely liable for the consequences arising from the misuse of his/her account by the Subscriber himself/herself or by a third-party who has accessed the Subscriber’s account due to the Subscriber’s fault or negligence, and as a result, the Subscriber acknowledges that eSound and all its partners, co-contracting party(ies) or rights-holders cannot be held liable in this regard.\n" +
            "Article 8 – Price\n" +
            "\n" +
            "The subscription price is stated in the Application and is inclusive of all taxes.\n" +
            "\n" +
            "eSound has the right to change the price of all subscription offers from time to time.\n" +
            "\n" +
            "Any increase in the rate of VAT (Value Added Tax) will be passed on automatically and immediately in the price of the eSound Premium Service. The same will apply should a new tax be created, based on the price of the eSound Premium Service and applicable to eSound. The conditions for cancellation applicable in the event of a change in price of the eSound Premium Service mentioned above will apply under the same conditions in the event of a price change arising from an increase in or the creation of new taxes.\n" +
            "\n" +
            "Subscribers are reminded that the connection and communication (Internet) costs relating to the use of the eSound Premium Service are not borne by eSound and are payable solely by the Subscriber.\n" +
            "Article 9 – Payment of the price\n" +
            "\n" +
            "The first payment will be made at the time of subscription, and thereafter payments will be made at the beginning of each month or year, by direct debit, until the subscription is cancelled, whether cancellation is initiated by the Subscriber or eSound.\n" +
            "\n" +
            "For optimised transaction security, eSound has chosen the payment systems of various companies with which it has entered into agreements. The guarantees given by eSound in terms of transaction security are identical to those obtained by eSound from these payment service providers.\n" +
            "\n" +
            "Unless the Subscriber provides evidence to the contrary, the computerised records kept within the computer systems of eSound and its partners in conditions of reasonable security will be treated as proof of communications, orders, validations and payments that have taken place between the Subscriber and eSound. This information shall be treated as valid proof between the Subscriber and eSound unless the Subscriber is able to provide written proof to the contrary.\n" +
            "Article 10 – Subscriber liability\n" +
            "\n" +
            "i) Each Subscriber can post messages, information and/or comments on the Site and the desktop Application. The Subscriber is solely liable for any messages, content or information published by him/her on the Site or desktop Application; eSound is considered to be an Internet service-hosting provider only and cannot be held liable for the content published by the Users on the Site or Application, over which eSound has no control or supervision.\n" +
            "\n" +
            "To avoid being held liable, the Subscriber expressly agrees to ensure that any message published by him/her on the Site or desktop Application complies with the following requirements (this list is not exhaustive):\n" +
            "\n" +
            "- Does not infringe third-party intellectual property rights; the Subscriber therefore agrees not to publish content on the Site or desktop Application that is protected under copyright law, a registered trademark or more generally any content protected by any other intellectual property rights held by third-parties without the prior consent of the owner or owners of said rights;\n" +
            "\n" +
            "- Contains no computer virus able to interrupt, destroy or affect the Site or desktop Application’s features;\n" +
            "\n" +
            "- Does not celebrate crime or criminal behaviour or contain illegal or threatening messages or content of a paedophile, pornographic, defamatory, obscene, hateful, racist, anti-Semitic, xenophobic or revisionist nature or contrary to public order and good manners;\n" +
            "\n" +
            "- Does not infringe any right to privacy or promote disrespect for human dignity;\n" +
            "\n" +
            "- Does not encourage violence, fanaticism, crime, suicide or hatred linked to religion, race, sex, sexual preferences or ethnicity;\n" +
            "\n" +
            "- Does not harass other Users;\n" +
            "\n" +
            "- Does not promote or encourage any criminal activity or enterprise;\n" +
            "\n" +
            "- Does not request and/or disclose passwords and/or personal information for commercial or illegal purposes;\n" +
            "\n" +
            "- Does not transmit email chains, mass unsolicited emails, instant messages, advertising messages and spam messages;\n" +
            "\n" +
            "- Contains no advertising and/or canvassing in order to offer products and/or services to sell through the Site or the Application;\n" +
            "\n" +
            "- Contains no addresses or Internet links transferring to an external website the content of which violates any applicable law and/or regulation, infringes third-party rights or is in breach of the Terms.\n" +
            "\n" +
            "ii) The Subscriber agrees not to use any automated system such as scripts for the purpose of adding Users to his/her User account and/or sending comments or messages.\n" +
            "\n" +
            "iii) If the Subscriber breaches any law or infringes any third-party rights, eSound has the right to provide any information enabling or facilitating the identification of the offender at the request of any legal authority (courts, administrative authorities and police forces).\n" +
            "Article 11 – Disclaimer of warranties\n" +
            "\n" +
            "The Subscriber declares that he has been informed of the scope and limitations of the Internet network.\n" +
            "\n" +
            "As a result, eSound disclaims all liability for any malfunction in access to the eSound Premium Service, the speed at which the pages of the Site or Application open and can be read, the listening speed of any tracks, the temporary or permanent inaccessibility of the eSound Premium Service, and the fraudulent use by third-parties of the information provided on the Site or Application.\n" +
            "\n" +
            "It is therefore the Subscriber’s duty to protect his/her equipment against any form of intrusion and/or virus contamination, for which eSound can never be held liable. eSound can never be held liable for any malfunction of or damage caused to the Subscriber’s equipment.\n" +
            "\n" +
            "More broadly, eSound disclaims any liability if a breach of any obligation results from acts of nature, forces, or causes beyond its reasonable control, including, without limitation, Internet failures, computer equipment failures, telecommunication equipment failures, other equipment failures, electrical power failures, strikes, labour disputes, riots, insurrections, civil disturbances, shortages of labour or materials, fires, floods, storms, explosions, acts of God, war, governmental actions, orders of domestic or foreign courts or tribunals, non-performance of third-parties, or loss of or fluctuations in heat, light or air conditioning and more broadly any unstoppable and unforeseen event which prevents orders being carried out successfully.\n" +
            "\n" +
            "eSound disclaims all liability should it transpire that the eSound Premium Service is incompatible with certain equipment and/or features of the Subscriber’s equipment.\n" +
            "\n" +
            "Finally, the Subscriber is solely liable for his/her use of the eSound Premium Service and cannot hold eSound liable for any claim and/or proceedings against him/her in this regard. The Subscriber shall be responsible for any claim, complaint or objection and more broadly any proceedings brought against eSound by a third-party in relation to the Subscriber’s use of the eSound Premium Service.\n" +
            "\n" +
            "Disclaimer of Warranties\n" +
            "YOU UNDERSTAND AND AGREE THAT YOUR USE OF THE SERVICES, THEIR CONTENT, AND ANY SERVICES OR ITEMS FOUND OR ATTAINED THROUGH THE SERVICES, IS AT YOUR OWN RISK. THE SERVICES, THEIR CONTENT, AND ANY SERVICES OR ITEMS FOUND OR ATTAINED THROUGH THE SERVICES, ARE PROVIDED ON AN \"AS IS\" AND \"AS AVAILABLE\" BASIS, WITHOUT ANY WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE FOREGOING DOES NOT AFFECT ANY WARRANTIES THAT CANNOT BE EXCLUDED OR LIMITED UNDER APPLICABLE LAW. NEITHER eSound NOR ITS SUBSIDIARIES, AFFILIATES OR THEIR RESPECTIVE DIRECTORS, OFFICERS, EMPLOYEES, AGENTS, SERVICE PROVIDERS, CONTRACTORS, LICENSORS, LICENSEES, SUPPLIERS, SUCCESSORS OR ASSIGNS, MAKE ANY WARRANTY, REPRESENTATION OR ENDORSEMENT WITH RESPECT TO THE COMPLETENESS, SECURITY, RELIABILITY, SUITABILITY, ACCURACY, CURRENCY OR AVAILABILITY OF THE SERVICES OR THEIR CONTENTS. WITHOUT LIMITING THE FOREGOING, NEITHER eSound NOR ITS SUBSIDIARIES, AFFILIATES OR THEIR RESPECTIVE DIRECTORS, OFFICERS, EMPLOYEES, AGENTS, SERVICE PROVIDERS, CONTRACTORS, LICENSORS, LICENSEES, SUPPLIERS, SUCCESSORS OR ASSIGNS, REPRESENT OR WARRANT THAT THE SERVICES, THEIR CONTENT, OR ANY SERVICES OR ITEMS FOUND OR ATTAINED THROUGH THE SERVICES, WILL BE ACCURATE, RELIABLE, ERROR-FREE, OR UNINTERRUPTED, THAT DEFECTS WILL BE CORRECTED, THAT THE SERVICES OR THE SERVER THAT MAKES THEM AVAILABLE ARE FREE OF VIRUSES OR OTHER HARMFUL COMPONENTS. WE CANNOT AND DO NOT GUARANTEE OR WARRANT THAT FILES OR DATA AVAILABLE FOR DOWNLOADING FROM THE INTERNET OR THE SERVICES WILL BE FREE OF VIRUSES OR OTHER DESTRUCTIVE CODE. YOU ARE SOLELY AND ENTIRELY RESPONSIBLE FOR YOUR USE OF THE SERVICES AND YOUR DEVICES, INTERNET AND DATA SECURITY. TO THE FULLEST EXTENT PROVIDED BY LAW, WE WILL NOT BE LIABLE FOR ANY LOSS OR DAMAGE CAUSED BY DENIAL-OF-SERVICE ATTACK, DISTRIBUTED DENIAL-OF-SERVICE ATTACK, OVERLOADING, FLOODING, MAILBOMBING OR CRASHING, VIRUSES, TROJAN HORSES, WORMS, LOGIC BOMBS, OR OTHER TECHNOLOGICALLY HARMFUL MATERIAL THAT MAY INFECT YOUR DEVICES, COMPUTER PROGRAMS, DATA, OR OTHER PROPRIETARY MATERIAL DUE TO YOUR USE OF THE SERVICES OR ANY SERVICES OR ITEMS FOUND OR ATTAINED THROUGH THE SERVICES OR TO YOUR DOWNLOADING OF ANY MATERIAL POSTED ON THE SERVICES, OR ON ANY WEBSITE LINKED TO THE SERVICES.\n" +
            "\n" +
            "Limitation on Liability\n" +
            "EXCEPT WHERE SUCH EXCLUSIONS ARE PROHIBITED BY LAW, UNDER NO CIRCUMSTANCE WILL eSound NOR ITS SUBSIDIARIES, AFFILIATES OR THEIR RESPECTIVE DIRECTORS, OFFICERS, EMPLOYEES, AGENTS, SERVICE PROVIDERS, CONTRACTORS, LICENSORS, LICENSEES, SUPPLIERS, SUCCESSORS OR ASSIGNS, BE LIABLE FOR NEGLIGENCE, GROSS NEGLIGENCE, NEGLIGENT MISREPRESENTATION, FUNDAMENTAL BREACH, DAMAGES OF ANY KIND, UNDER ANY LEGAL THEORY, INCLUDING ANY DIRECT, INDIRECT, SPECIAL, INCIDENTAL, CONSEQUENTIAL, OR PUNITIVE DAMAGES, INCLUDING, BUT NOT LIMITED TO, PERSONAL INJURY, PAIN AND SUFFERING, EMOTIONAL DISTRESS, LOSS OF REVENUE, LOSS OF PROFITS, LOSS OF BUSINESS OR ANTICIPATED SAVINGS, LOSS OF USE, LOSS OF GOODWILL, LOSS OF DATA, AND WHETHER CAUSED BY TORT (INCLUDING NEGLIGENCE), BREACH OF CONTRACT, BREACH OF PRIVACY OR OTHERWISE, ARISING OUT OF OR IN CONNECTION WITH YOUR USE, OR INABILITY TO USE, OR RELIANCE ON, THE SERVICES, ANY LINKED WEBSITES OR SUCH OTHER THIRD-PARTY WEBSITES, NOR ANY WEBSITE CONTENT, MATERIALS, POSTING OR INFORMATION THEREON, EVEN IF THE PARTY WAS ALLEGEDLY ADVISED OR HAD REASON TO KNOW OF SUCH HARM OR LOSSES.\n" +
            "\n" +
            "Indemnification\n" +
            "To the maximum extent permitted by applicable law, you agree to defend, indemnify, and hold harmless eSound, its subsidiaries, affiliates, and their respective directors, officers, employees, agents, service providers, contractors, licensors, suppliers, successors and assigns, from and against any claims, liabilities, damages, judgments, awards, losses, costs, expenses, or fees (including reasonable attorneys\\' fees) arising out of or relating to your breach of these Terms and Conditions or your use of the Services, including, but not limited to, third-party sites, any use of content on, or in, the Services, and products, other than as expressly authorized in these Terms and Conditions.\n" +
            "\n" +
            "Waiver\n" +
            "No failure to exercise, or delay in exercising, any right, remedy, power or privilege arising from these Terms and Conditions operates, or may be construed, as a waiver thereof. No single or partial exercise of any right, remedy, power or privilege hereunder precludes any other or further exercise thereof or the exercise of any other right, remedy, power or privilege.\n" +
            "You agree to waive any right you may have to: (a) a trial by jury; and (b) commence or participate in any class action against us related to your use of the Services, the exchange of electronic documents between us or these Terms and Conditions, and, where applicable, you also agree to opt-out of any class proceedings against us or our licensors.\n" +
            "\n" +
            "Severability and Survival\n" +
            "If any term or provision of these Terms and Conditions is invalid, illegal or unenforceable in any jurisdiction, such invalidity, illegality or unenforceability shall not affect any other term or provision of these Terms and Conditions or invalidate or render unenforceable such term or provision in any other jurisdiction. Any provision of these Terms and Conditions that must survive to fulfill its essential purpose (whether expressly stated as such or not) and any obligation you have to pay fees incurred before termination, will survive the termination of these Terms and Conditions.\n" +
            "\n" +
            "Entire Agreement\n" +
            "These Terms and Conditions and our Privacy Policy constitute the sole and entire agreement between you and us, regarding the Services, and supersede all prior and contemporaneous understandings, agreements, representations and warranties, both written and oral, regarding such subject matter.\n" +
            "\n" +
            "Reporting and Contact\n" +
            "If there are any questions regarding these Terms &amp; Conditions, you may contact us using the information below.\n" +
            "\n" +
            "eSound Music\n" +
            "copyright@esound.app\n";
}

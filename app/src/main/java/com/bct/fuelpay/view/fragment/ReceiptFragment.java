package com.bct.fuelpay.view.fragment;

import static android.content.Context.WINDOW_SERVICE;
import static com.bct.fuelpay.utils.FragmentUtils.TRANSITION_NONE;
import static com.bct.fuelpay.utils.FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.basewin.aidl.OnPrinterListener;
import com.basewin.define.GlobalDef;
import com.basewin.models.BitmapPrintLine;
import com.basewin.models.PrintLine;
import com.basewin.models.TextPrintLine;
import com.basewin.services.PrinterBinder;
import com.basewin.services.ServiceManager;
import com.basewin.utils.TimerCountTools;
import com.bct.fuelpay.R;
import com.bct.fuelpay.databinding.FragmentReceiptBinding;
import com.bct.fuelpay.utils.FragmentUtils;
import com.bct.fuelpay.utils.PicUtils;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.pos.sdk.printer.PosPrinter;
import com.pos.sdk.printer.PosPrinterInfo;

//import CTOS.CtPrint;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class ReceiptFragment extends Fragment {

    private TimerCountTools timeTools;
    private TimerCountTools timeTools1;
    private int printType = 1;
    private int printMaxNum = 0;
    private PrinterListener printer_callback = new PrinterListener();
    private FragmentReceiptBinding binding;
    Bundle b;
    String strMOP, strWhichScreen, strTransactionID, strTransactionDate, strProduct, strAmount, strVolume,
            strPump, strNozzle, strAttendant, strPrice, strPreAuthAmount, product,strTransactionTime;
    Bitmap bitmap,bitmapArabic;
    QRGEncoder qrgEncoder,qrgArabicEncoder;
    StringBuilder textToSend;
    StringBuilder textArabicToSend;
//    private CtPrint print;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_receipt,container,false);
        setUpWidgets();
//        SlideToDown();

        return binding.getRoot();
    }

    private void setUpWidgets(){
        b = new Bundle();
        b = getArguments();

//        strWhichScreen = b.getString("WHICHSCREEN");
        try {
            strMOP = b.getString("mop");
            if (strMOP!=null){
                strMOP = getMop(strMOP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            switch (b.getString("mop")) {
//                case "1":
//                    strMOP = "CASH";
//                    break;
//
//                case "2":
//                    strMOP = "CREDIT CARD";
//                    break;
//
//                case "3":
//                    strMOP = "LOCAL ACCOUNT";
//                    break;
//
//                case "4":
//                    strMOP = "LOYALTY";
//                    break;
//
//                case "5":
//                    strMOP = "WALLET";
//                    break;
//
//                case "6":
//                    strMOP = "FLEET POS";
//                    break;
//            }
//        } catch (Exception e) {
//            strMOP = b.getString("mop");
//        }
        strPump = b.getString("Pump");
        strNozzle = b.getString("Nozzle");
        strTransactionID = b.getString("transactionId");
        strTransactionDate = b.getString("date");
        strTransactionTime = b.getString("time");
        strProduct = b.getString("product");
        Log.e("PRODUCT", "" + strProduct);
        strAmount = b.getString("Amount");
        strVolume = b.getString("volume");
        strPrice = b.getString("price");
        strAttendant = b.getString("attandant");
        strPreAuthAmount = b.getString("amount");
        product = b.getString("Grade");


        binding.tvDate.setText("Date & time:     "+strTransactionDate+" "+strTransactionTime);
        binding.tvTranID.setText("Transaction ID:     "+strTransactionID);
        binding.tvPumpNo.setText("Pump No:     "+strPump);
        binding.tvNozzleNo.setText("Nozzle No:     "+strNozzle);
        binding.tvPrice.setText("Price:     "+strPrice+" "+requireActivity().getResources().getString(R.string.currency));
        binding.tvProduct.setText("Product Name:     "+product);
        binding.tvVolume.setText("Volume:     "+strVolume+" L");
        binding.tvAmount.setText("Amount:     "+strAmount+" "+requireActivity().getResources().getString(R.string.currency));
        binding.tvModePayment.setText("Mode of Payment:     "+strMOP);
        binding.tvAttandant.setText("Attandent:     "+strAttendant);
        textToSend = new StringBuilder();
        textToSend.append("FuelTrans"+"\n"+ "Receipt Number "+strTransactionID+"\n"+
                ", Date "+strTransactionDate+"\n"+", Product "+product+"\n"+
                ", Price "+strPrice+"SAR"+"\n"+" Volume "+
                strVolume+" l"+"\n"+" Amount "+strAmount+"SAR"+"\n\n");
        textArabicToSend = new StringBuilder();
        textArabicToSend.append("فيولترانس"+"\n"+strTransactionID+"رقم المعاملة"+"\n"+strTransactionDate+" تاريخ"
                +"\n"+"بنزين"+" بنزين"+"\n"+strPrice+" SAR "+"سعر"+"\n"+strVolume+" L "+" الصوت"
                +"\n"+strAmount+" SAR "+ "كمية");
        generateQR();

        binding.tvArabicDate.setText("تاريخ:    "+strTransactionDate+" "+strTransactionTime);
        binding.tvArabicTran.setText("رقم المعاملة:    "+strTransactionID);
        binding.tvArabicPump.setText("رقم المضخة:    "+strPump);
        binding.tvArabicNozzle.setText("رقم الفوهة:   "+strNozzle);
        binding.tvArabicPrice.setText("سعر:    "+strPrice+" SAR");
        binding.tvArabicProductName.setText("بنزين:    "+product);
        binding.tvArabicVolume.setText("الصوت:       "+strVolume+"L");
        binding.tvArabicAmount.setText("كمية:      "+strAmount+" SAR");
        binding.tvArabicMOP.setText("طريقة الدفع:    "+strMOP);
        binding.tvArabicAttandant.setText("طريقة الدفع:      "+strAttendant);



        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SlideToAbove();
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(),new HomeFragment(),
                        R.id.fragContainer, false, TRANSITION_NONE);
            }
        });

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(),new HomeFragment(),
                        R.id.fragContainer, false, TRANSITION_SLIDE_LEFT_RIGHT);
            }
        });

//        try {
//            switch (strProduct) {
//                case "1":
//                    product = "MS";
//                    break;
//
//                case "2":
//                    product = "HSD";
//                    break;
//
//                case "3":
//                    product = "ESSENCE SUPER";
//                    break;
//            }
//        } catch (Exception e) {
//            product = "NA";
//        }

    }

    private String getMop(String mop){
        switch (mop){
            case "1":
                return "Cash";

            case "2":
                return "Credit card";

            case "3":
                return "Local Account";

            case "4":
                return "Loyalty";

            case "5":
                return "Wallet";

            case "6":
                return "Fleet POS";

            case "7":
                return "Testing";

            case "8":
                return "Sampling";

            case "9":
                return "OwnUse";

            case "10":
                return "Others";

            default:
                return "NA";
        }

    }

    private void generateQR(){
        WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(textToSend.toString(), null, QRGContents.Type.TEXT, dimen);
        qrgArabicEncoder = new QRGEncoder(textArabicToSend.toString(), null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            bitmapArabic = qrgArabicEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            binding.qrCodeIV.setImageBitmap(bitmap);
            binding.qrArabicCodeIV.setImageBitmap(bitmapArabic);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }
    }

    public void SlideToAbove() {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.1f);

        slide.setDuration(4400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        binding.llReceipt.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                printVizPayReceipt();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.llReceipt.clearAnimation();
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                        llReceipt.getWidth(), llReceipt.getHeight());
//                // lp.setMargins(0, 0, 0, 0);
//                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                llReceipt.setLayoutParams(lp);

            }

        });

    }

    public void SlideToDown() {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);

        slide.setDuration(4000);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        binding.llReceipt.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                binding.llReceipt.clearAnimation();
                SlideToAbove();
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                        llReceipt.getWidth(), llReceipt.getHeight());
//                lp.setMargins(0, llReceipt.getWidth(), 0, 0);
//                lp.addRule(RelativeLayout.ALIGN_PARENT_START);
//                llReceipt.setLayoutParams(lp);
            }

        });

    }

    private void printVizPayReceipt() {

        try {
            ServiceManager.getInstence().init(getActivity().getApplicationContext());
            ServiceManager.getInstence().getPrinter().setPrintTypesettingType(GlobalDef.ANDROID_TYPESETTING);

            timeTools = new TimerCountTools();
            timeTools.start();
            ServiceManager.getInstence().getPrinter().cleanCache();
            ServiceManager.getInstence().getPrinter().setPrintGray(1200);
            ServiceManager.getInstence().getPrinter().setLineSpace(2);

            BitmapPrintLine bitmapPrintLine = new BitmapPrintLine();
            bitmapPrintLine.setType(PrintLine.BITMAP);
            bitmapPrintLine.setPosition(PrintLine.CENTER);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.benzene_logo);
            bitmapPrintLine.setType(PrintLine.BITMAP);
//            bitmapPrintLine.setBitmap(bitmap);
            bitmapPrintLine.setBitmap(PicUtils.switchColor(bitmap));
            ServiceManager.getInstence().getPrinter().addPrintLine(bitmapPrintLine);


            TextPrintLine textPrintLine = new TextPrintLine();
            textPrintLine.setType(PrintLine.TEXT);
            textPrintLine.setPosition(TextPrintLine.CENTER);
            textPrintLine.setBold(true);
            textPrintLine.setSize(35);

            textPrintLine.setContent("");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("Tax Invoice");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);
            textPrintLine.setContent("");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);


            textPrintLine.setType(PrintLine.TEXT);
            textPrintLine.setPosition(TextPrintLine.LEFT);
            textPrintLine.setSize(24);

            textPrintLine.setContent("Branch:    " + "Azizla Tahilah Branch");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("Address:    " + "Umm Al Salam, Old Makkah Road, Jeddah,\\n Jeddah 98232");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("Vat #:    " + "421731256356173582178");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("-----------------------------------------");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);


            textPrintLine.setContent(getString(R.string.date) + "    " + strTransactionDate+ " "+strTransactionTime);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("Transaction ID:    " + strTransactionID);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("Pump No:  " + strPump);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("Nozzle No: "+strNozzle);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("Product:    " + product);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("Price:    " + strPrice+" "+getResources().getString(R.string.currency));
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("Volume:    " + strVolume + " L");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("Amount:    " + strAmount +" "+getResources().getString(R.string.currency));
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("Mode of Payment: " + strMOP);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("Attendant: "+strAttendant);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setType(PrintLine.TEXT);
            textPrintLine.setPosition(TextPrintLine.CENTER);
            textPrintLine.setBold(true);
            textPrintLine.setSize(22);

            textPrintLine.setContent("----------------------------------------------");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("The above price include vat \n Thanks for visit");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("----------------------------------------------");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);


            textPrintLine.setContent("CUSTOMER COPY");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);
            textPrintLine.setContent("");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(textToSend.toString(), BarcodeFormat.QR_CODE, 300, 300);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap1 = barcodeEncoder.createBitmap(bitMatrix);
                BitmapPrintLine bitmapPrintLine1 = new BitmapPrintLine();
                bitmapPrintLine1.setType(PrintLine.BITMAP);
                bitmapPrintLine1.setPosition(PrintLine.CENTER);
                bitmapPrintLine1.setBitmap(PicUtils.switchColor(bitmap1));
                ServiceManager.getInstence().getPrinter().addPrintLine(bitmapPrintLine1);

            } catch (Exception e) {
                e.printStackTrace();
            }

            textPrintLine.setType(PrintLine.TEXT);
            textPrintLine.setPosition(TextPrintLine.CENTER);
            textPrintLine.setSize(24);
            textPrintLine.setContent(" ");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);
            textPrintLine.setContent(" ");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);
            textPrintLine.setContent(" ");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);
            textPrintLine.setContent(" ");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);




            textPrintLine.setType(PrintLine.TEXT);
            textPrintLine.setPosition(TextPrintLine.RIGHT);
            textPrintLine.setSize(24);

            textPrintLine.setContent("فرع شجرة:    " + "فرع عزيزلة التحيلة");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("تبوك:    " + "أم السلام ، طريق مكة القديم ، جدة ، \\ n جدة 98232");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("ضريبة القيمة المضافة #:    " + "421731256356173582178");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("-----------------------------------------");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setType(PrintLine.TEXT);
            textPrintLine.setPosition(TextPrintLine.RIGHT);
            textPrintLine.setBold(true);
            textPrintLine.setSize(22);

            textPrintLine.setContent("تاريخ"+ "     " +strTransactionDate+ " " + strTransactionTime);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("رقم المعاملة"+"       "+strTransactionID);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("رقم المضخة"+"       "+strPump);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("رقم الفوهة"+"       " +strNozzle);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent(product+"       " + "بنزين");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("سعر"+"       "  +strPrice+" "+getResources().getString(R.string.currency));
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent(strVolume+" L      " + "الصوت");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent( " كمية"+"       "+strAmount+" "+getResources().getString(R.string.currency));
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

//            textPrintLine.setContent("Mode of Payment: " + strMOP);
//            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("طريقة الدفع"+ strAttendant);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);



            textPrintLine.setType(PrintLine.TEXT);
            textPrintLine.setPosition(TextPrintLine.CENTER);
            textPrintLine.setBold(true);
            textPrintLine.setSize(20);

            textPrintLine.setContent("----------------------------------------------");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("أقر باستلام الوقود المُرضي");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);

            textPrintLine.setContent("----------------------------------------------");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);


            textPrintLine.setContent("نسخة العميل");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);
            textPrintLine.setContent("");
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);
            ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);


//            Bitmap bitmap;
//            StringBuilder textToSend = new StringBuilder();
//            textToSend.append("DT360"+"\n"+ "Receipt Number "+strTransactionID+"\n"+
//                    ", Date "+strTransactionDate+"\n"+", Product "+product+"\n"+
//                    ", Price "+strPrice+"INR"+"\n"+" Volume "+
//                    strVolume+" l"+"\n"+" Amount "+strAmount+"INR"+"\n\n"+strTransactionID+"رقم المعاملة"+"\n"+strTransactionDate+" تاريخ"
//                    +"\n"+"بنزين"+" بنزين"+"\n"+strPrice+" INR "+"سعر"+"\n"+strVolume+" L "+" الصوت"
//                    +"\n"+strAmount+" INR "+ "كمية");
//            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(textArabicToSend.toString(), BarcodeFormat.QR_CODE, 300, 300);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap1 = barcodeEncoder.createBitmap(bitMatrix);
                BitmapPrintLine bitmapPrintLine1 = new BitmapPrintLine();
                bitmapPrintLine1.setType(PrintLine.BITMAP);
                bitmapPrintLine1.setPosition(PrintLine.CENTER);
                bitmapPrintLine1.setBitmap(PicUtils.switchColor(bitmap1));
                ServiceManager.getInstence().getPrinter().addPrintLine(bitmapPrintLine1);





                textPrintLine.setType(PrintLine.TEXT);
                textPrintLine.setPosition(TextPrintLine.CENTER);
                textPrintLine.setSize(24);
                textPrintLine.setContent(" ");
                ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);
                textPrintLine.setContent(" ");
                ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);
                textPrintLine.setContent(" ");
                ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);
                textPrintLine.setContent(" ");
                ServiceManager.getInstence().getPrinter().addPrintLine(textPrintLine);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ServiceManager.getInstence().getPrinter().beginPrint(printer_callback);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /*private void printSurePayReceipt(){

        print = new CtPrint();
        String print_font;
        int print_x = 0;
        int print_y = 36;
        int Currently_high = 20;
        int ret = 0;
        Typeface font;
        AssetManager assetManager = getActivity().getAssets();
        font = Typeface.createFromAsset(assetManager, String.format("fonts/%s", "times_new_roman.otf"));
        print.initPage(2700);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.benzene_logo);

        print.drawImage(Bitmap.createScaledBitmap(bitmap, 220, 300, false),70,print_y + Currently_high);
        Currently_high += 330;
//        print.drawText(0,print_y + Currently_high,getString(R.string.date)+"    "
//                +b.getString("date"),18);
        print.setAlign(CtPrint.PRT_ALIGNCENTER);
        print.drawText(0, print_y  +Currently_high, "Tax Invoice", 25, 1,0xFF000000,
                true, (float) 0, false,
                false,font);
        Currently_high += print_y+20;
        print.setAlign(CtPrint.PRT_ALIGNLEFT);
        print.drawText(0, print_y  +Currently_high,"Branch:    " + "Benzene Gas Station", 25, 1,0xFF000000,
                true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y  +Currently_high,"Address:    "
                        + "King Fahd Branch Road"
                        ,
                25, 1,0xFF000000,
                true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y  +Currently_high,"Al Aarid ,Riyadh, KSA", 25, 1,0xFF000000,
                true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y  +Currently_high,"Vat #:    " + "421731256356173582178", 25, 1,0xFF000000,
                true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y  +Currently_high,"-----------------------------------------", 25, 1,0xFF000000,
                true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.setAlign(CtPrint.PRT_ALIGNLEFT);
        print.drawText(0, print_y  +Currently_high, getString(R.string.date)+"    "
                        +b.getString("date"), 25, 1,0xFF000000,
                true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "Transaction ID:    "+b.getString("transactionId"),
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);

        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "Pump No:  "+b.getString("Pump"),
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);

        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "Nozzle No:  1",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);

        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "Product:    "+b.getString("product"),
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);

        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "Price:    "+b.getString("price")+"SAR",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);

        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "Volume:    "+b.getString("volume")+"L",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);

        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "Amount:    "+b.getString("Amount")+"SAR",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);

        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "Mode of Payment: "+strMOP,
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);

        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "Attendant:  "+b.getString("attandant"),
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(textToSend.toString(),
                    BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap1 = barcodeEncoder.createBitmap(bitMatrix);
            print.drawImage(PicUtils.switchColor(bitmap1),45,print_y + Currently_high);


        } catch (Exception e) {
            e.printStackTrace();
        }
        Currently_high += 300;
        print.setAlign(CtPrint.PRT_ALIGNCENTER);
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "I ACKNOWLEDGE SATISFACTORY",
                20, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "RECEIPT OF FUELING",
                20, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "CUSTOMER COPY",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        Currently_high += print_y;
        Currently_high += print_y;
        Currently_high += print_y;
        print.setAlign(CtPrint.PRT_ALIGNRIGHT);

        print.drawText(0, print_y + Currently_high, "فرع شجرة:    " + "محطة شركة البنزين",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;

        print.drawText(0, print_y + Currently_high,   "تبوك:    " + "طريق الملك فهد الفرعي\n" +
                        "العارض ، الرياض\n" +
                        "المملكة العربية السعودية\n",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;



        print.drawText(0, print_y + Currently_high,     "ضريبة القيمة المضافة #:    " + "421731256356173582178",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;

        print.drawText(0, print_y + Currently_high,     "-----------------------------------------",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;

        print.drawText(0, print_y + Currently_high, "تاريخ"+":    "+b.getString("date"),
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "رقم المعاملة"+": " +b.getString("transactionId"),
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, " رقم المضخة"+":  "+b.getString("Pump"),
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "1  "+":  "+"رقم الفوهة",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "بنزين"+":  "+":  "+" المنتج",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, b.getString("price")+" SAR: "+"سعر",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, b.getString("volume")+" L : "+"الصوت",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, b.getString("Amount")+" SAR: "+"كمية",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, b.getString("attandant")+":  "+"طريقة الدفع",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        Currently_high += print_y;
//        print.drawText(0, print_y + Currently_high, "Please scan below QR for link to   online receipt:",
//                18, 1,0xFF000000, true, (float) 0, false,
//                false,font);
//        Currently_high += print_y;

//        print.encodeToBitmap("qrContent", BarcodeFormat.QR_CODE,50,50);
        print.setAlign(CtPrint.PRT_ALIGNCENTER);
        multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(textArabicToSend.toString(),
                    BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap1 = barcodeEncoder.createBitmap(bitMatrix);
            print.drawImage(PicUtils.switchColor(bitmap1),45,print_y + Currently_high);


        } catch (Exception e) {
            e.printStackTrace();
        }

        Currently_high += 300;
        print.setAlign(CtPrint.PRT_ALIGNCENTER);
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "أعلم أنني مرضية",
                20, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "استلام الوقود",
                20, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;
        Currently_high += print_y;
        print.drawText(0, print_y + Currently_high, "نسخة العميل",
                25, 1,0xFF000000, true, (float) 0, false,
                false,font);
        Currently_high += print_y;




        print.printPage();
        print.roll(60);


    }*/

    class PrinterListener implements OnPrinterListener {
        private final String TAG = "Print";

        @Override
        public void onStart() {
            // TODO 打印开始
            // Print start
            Log.e(TAG, "onStart");
            timeTools1 = new TimerCountTools();
            timeTools1.start();
        }

        @Override
        public void onFinish() {
            // TODO 打印结束
            // End of the print
            Log.e(TAG, "onFinish");
            ;
            timeTools.stop();
            timeTools1.stop();
            //tv_display.setText(Long.toString(timeTools.getProcessTime()));

//            if(printType == 0){
//                str_c += Long.toString(timeTools.getProcessTime()) + "["+ Long.toString(timeTools1.getProcessTime())+ "]"+ "  " ;
//            }else if(printType == 1){
//                str_android += Long.toString(timeTools.getProcessTime()) + "["+ Long.toString(timeTools1.getProcessTime())+ "]"+ "  " ;
//            }else{
//                str_layout +=  Long.toString(timeTools.getProcessTime()) + "["+ Long.toString(timeTools1.getProcessTime())+ "]"+ "  " ;
//            }
//
//            str_display = str_c + "\n\n" + str_android + "\n\n" + str_layout;
//
//            tv_display.setText(str_display);

            PosPrinterInfo info = new PosPrinterInfo();
            PosPrinter.getPrinterInfo(0, info);
            int temperature = info.mTemperature;

            Log.e(TAG, "info.mTemperature:" + info.mTemperature);
            try {
                int hight = ServiceManager.getInstence().getPrinter().getPrintPixelHeight();
                Log.e(TAG, "hight:" + hight);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            handler.sendEmptyMessage(0);
        }

        @Override
        public void onError(int errorCode, String detail) {
            // TODO 打印出错
            // print error
            Log.e(TAG, "print error" + " errorcode = " + errorCode + " detail = " + detail);
            if (errorCode == PrinterBinder.PRINTER_ERROR_NO_PAPER) {
                //Toast.makeText(MainActivity.this, "paper runs out during printing", Toast.LENGTH_SHORT).show();
            }
            if (errorCode == PrinterBinder.PRINTER_ERROR_OVER_HEAT) {
                Toast.makeText(getActivity(), "over heat during printing", Toast.LENGTH_SHORT).show();
            }
            if (errorCode == PrinterBinder.PRINTER_ERROR_OTHER) {
                Toast.makeText(getActivity(), "other error happen during printing", Toast.LENGTH_SHORT).show();
            }

            //handler.sendMessageDelayed(null, 1000);
//            handler.sendEmptyMessageDelayed(1,1000);
        }
    }
}
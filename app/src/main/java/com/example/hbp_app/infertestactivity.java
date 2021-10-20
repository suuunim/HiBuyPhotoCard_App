package com.example.hbp_app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class infertestactivity extends AppCompatActivity {


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    Uri selectedImageUri;
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference mDatabase;

    private FirebaseDatabase mData;
    EditText infer_Result;
    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;

    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infertest);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);





        ActionBar actionBar = getSupportActionBar();
        actionBar.hide(); // actionBar 숨기기


        imageview = (ImageView) findViewById(R.id.infer_image);
        imageview.setBackground(new ShapeDrawable(new OvalShape()));

        infer_Result = findViewById(R.id.infer_result);

        storage = FirebaseStorage.getInstance();
        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);

            }
        });

        Button infer_finish=findViewById(R.id.buttoninfer);
        infer_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) imageview.getDrawable();
                Bitmap bitmap_1 = drawable.getBitmap();
                Bitmap bitmap = Bitmap.createScaledBitmap(bitmap_1, 224, 224, true);
                ByteBuffer input = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder());
                int batchNum = 0;
//
//            //이미지 뷰에 선택한 사진 띄우기
//
                float[][] bytes_img = new float[1][50176];
//            // x,y 최댓값 사진 크기에 따라 달라짐 (조절 해줘야함)
                for (int x = 0; x < 224; x++) {
                    for (int y = 0; y < 224; y++) {
                        int pixel = bitmap.getPixel(x, y);
                        bytes_img[0][y*224+x] = (pixel & 0xff) / (float) 255;
                    }
                }
                float[][] output = new float[1][249];

//            // 자신의 tflite 이름 써주기
                Interpreter lite = getTfliteInterpreter("model.tflite");
                lite.run(bytes_img, output);

                int max_index =0;
                float max = output[1][0];
                for(int i=1 ; i<output.length ; i++) {
                    if(output[1][i]>max) {
                        max = output[1][i];
                        max_index=i;
                    }
                }
                infer_Result.setText(max_index);


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);
            StorageReference storageRef = storage.getReference();
            StorageReference riversRef = storageRef.child(selectedImageUri.toString());
            UploadTask uploadTask = riversRef.putFile(selectedImageUri);
        }


    }



    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(infertestactivity.this, modelPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 모델을 읽어오는 함수로, 텐서플로 라이트 홈페이지에 있다.
    // MappedByteBuffer 바이트 버퍼를 Interpreter 객체에 전달하면 모델 해석을 할 수 있다.
    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

}
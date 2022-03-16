package com.unidue.socialrobotics;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.common.collect.Lists;

import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.RosActivity;
import org.ros.android.view.RosImageView;
import org.ros.android.view.visualization.Viewport;
import org.ros.android.view.visualization.VisualizationView;
import org.ros.android.view.visualization.layer.CameraControlLayer;
import org.ros.android.view.visualization.layer.LaserScanLayer;
import org.ros.android.view.visualization.layer.Layer;
import org.ros.android.view.visualization.layer.OccupancyGridLayer;
import org.ros.android.view.visualization.layer.PathLayer;
import org.ros.android.view.visualization.layer.PosePublisherLayer;
import org.ros.android.view.visualization.layer.PoseSubscriberLayer;
import org.ros.android.view.visualization.layer.RobotLayer;
import org.ros.namespace.NameResolver;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;

public class MainActivity extends RosActivity {


    private NodeConfiguration nodeConfiguration;
    private VisualizationView mapView;
    private RosImageView image;

    public MainActivity() {
        super("socialRobotics", "SocialRobotics", URI.create("http://"+LoginActivity.sharedPref.getString("MasterIP","")+":"+LoginActivity.sharedPref.getString("MasterPort","")));
        //super("socialRobotics", "SocialRobotics", URI.create("http://192.168.1.64:11311"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (RosImageView) findViewById(R.id.image);
        image.setTopicName("/camera/rgb/image_raw/compressed");
        image.setMessageType("sensor_msgs/CompressedImage");
        image.setMessageToBitmapCallable(new BitmapFromCompressedImage());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        image.getLayoutParams().height = (int)((480*width)/640);

        mapView = findViewById(R.id.map_view);
        mapView.getCamera().jumpToFrame("map");

        mapView.getCamera().setViewport(new Viewport(1,1));
        mapView.getCamera().zoom(0,0,0.2);
        mapView.getCamera().rotate(0,0,-1.57);
        mapView.getCamera().translate(-500.0, -275.0);
        mapView.onCreate(Lists.<Layer>newArrayList(new CameraControlLayer(),
                new OccupancyGridLayer("map"),
                //new PathLayer("move_base/NavfnROS/plan"),
                new PathLayer("move_base_dynamic/NavfnROS/plan"),
                new LaserScanLayer("scan"),
                new PoseSubscriberLayer("move_base_simple/goal"),
                new PosePublisherLayer("move_base_simple/goal"),
                new RobotLayer("base_footprint")));

        Button btnSetTask = findViewById(R.id.btnSetTask);
        btnSetTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button btnViewTasks = findViewById(R.id.btnViewTasks);
        btnViewTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public void init(NodeMainExecutor nodeMainExecutor) {
        nodeConfiguration = NodeConfiguration.newPublic(getRosHostname(),getMasterUri());
        NameResolver appNameResolver = nodeConfiguration.getParentResolver();

        mapView.init(nodeMainExecutor);
        nodeMainExecutor.execute(mapView, nodeConfiguration.setNodeName("android/map_view"));

        nodeMainExecutor.execute(image,nodeConfiguration.setNodeName("android/image_view"));
    }

}
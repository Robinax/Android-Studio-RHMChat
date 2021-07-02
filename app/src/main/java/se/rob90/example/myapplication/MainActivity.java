    package se.rob90.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import se.rob90.example.myapplication.Fragments.ChatsFragment;
import se.rob90.example.myapplication.Fragments.UsersFragment;
import se.rob90.example.myapplication.Model.Users;

    public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";


    //Firebase
    FirebaseUser firebaseUser;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser
                .getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            Users users = snapshot.getValue(Users.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Tab layout and viewpager
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter ViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        ViewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        ViewPagerAdapter.addFragment(new UsersFragment(), "Users");

        viewPager.setAdapter(ViewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }
        //Menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.logout:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,Login_Activity.class));
                    finish();
                    return true;

                case R.id.storeddata:
                    startActivity(new Intent(MainActivity.this, Stored_Chats.class));
                    finish();
                    return true;


            }
            return false;
        }


        //Class viewpageradapter
        class ViewPagerAdapter extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragments;
        private  ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles    = new ArrayList<>();

        }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
            public void addFragment(Fragment fragment,String title){
                fragments.add(fragment);
                titles.add(title);

            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        }
    }







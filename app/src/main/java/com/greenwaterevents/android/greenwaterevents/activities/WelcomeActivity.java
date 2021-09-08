package com.greenwaterevents.android.greenwaterevents.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greenwaterevents.android.greenwaterevents.utils.PrefManager;
import com.greenwaterevents.android.greenwaterevents.R;
import com.squareup.picasso.Picasso;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int currentPage = 0;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private TextView title, description;
    private PrefManager prefManager;
    private FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout;
    private Animation translate_downwards, translate_upwards, set_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btnSkip = findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);
        title = findViewById(R.id.captionText);
        description = findViewById(R.id.descriptionText);
        fab = findViewById(R.id.fab);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        translate_downwards = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_downward);
        translate_upwards = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_upward);
        set_animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.set_animation);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setPageTransformer(true, new IntroPageTransformer());
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
//                coordinatorLayout.startAnimation(set_animation);
            }
        });
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);

        fab.setBackgroundTintList(ColorStateList.valueOf(colorsActive[currentPage]));
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, HomeScreen.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        boolean a, left;
        float b;

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            Log.e("TAG", currentPage + "");

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
//                btnSkip.setVisibility(View.VISIBLE);
            }
            if (viewPager.getCurrentItem() == 0) {
                title.setText(getString(R.string.slide_1_title));
                description.setText(getString(R.string.slide_1_desc));
            } else if (viewPager.getCurrentItem() == 1) {
                title.setText(getString(R.string.slide_2_title));
                description.setText(getString(R.string.slide_2_desc));
            } else if (viewPager.getCurrentItem() == 2) {
                title.setText(getString(R.string.slide_3_title));
                description.setText(getString(R.string.slide_3_desc));
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int postionOffsetPixel) {
            if (b < positionOffset) {
                left = true;
                Log.e("eric", "scrolling left ...");
            } else {
                left = false;
                Log.e("eric", "scrolling right ...");
            }
            Log.e("eric", left + " " + positionOffset);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (ViewPager.SCROLL_STATE_DRAGGING == state) {
                if (!a) {
                    coordinatorLayout.startAnimation(translate_downwards);
                    a = true;
                }
            }
            if (ViewPager.SCROLL_STATE_IDLE == state) {
                if (a) {
                    coordinatorLayout.startAnimation(translate_upwards);
                    a = false;
                }
            }
        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private int screenWidth;
        private int screenHeight;
        private int[] images = {R.drawable.image1,
                R.drawable.image2, R.drawable.image3};

        MyViewPagerAdapter(Context mContext) {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert layoutInflater != null;
            View view = layoutInflater.inflate(layouts[position], container, false);

            ImageView imageView = view.findViewById(R.id.imageView);

            Picasso.with(WelcomeActivity.this)
                    .load(images[position])
                    .resize(screenWidth, screenHeight)
                    .centerCrop()
                    .into(imageView);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public class IntroPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View page, float position) {
            // Get the page index from the tag. This makes
            // it possible to know which page index you're
            // currently transforming - and that can be used
            // to make some important performance improvements.
//            int pagePosition = (int) page.getTag();

            // Here you can do all kinds of stuff, like get the
            // width of the page and perform calculations based
            // on how far the user has swiped the page.
            int pageWidth = page.getWidth();
            float pageWidthTimesPosition = pageWidth * position;
            float absPosition = Math.abs(position);

            // Now it's time for the effects
            if (position <= -1.0f || position >= 1.0f) {

                // The page is not visible. This is a good place to stop
                // any potential work / animations you may have running.

            } else if (position == 0.0f) {

                // The page is selected. This is a good time to reset Views
                // after animations as you can't always count on the PageTransformer
                // callbacks to match up perfectly.

            } else {

                // The page is currently being scrolled / swiped. This is
                // a good place to show animations that react to the user's
                // swiping as it provides a good user experience.

//                View relativeLayout = page.findViewById(R.id.relativeLayout);
//                relativeLayout.setTranslationY(-pageWidthTimesPosition);
//                relativeLayout.setAlpha(1.0f - absPosition);

                // Let's start by animating the title.
                // We want it to fade as it scrolls out
                View heading = page.findViewById(R.id.heading);
                heading.setTranslationY(-pageWidthTimesPosition / 4f);
                heading.setAlpha(1.0f - absPosition);

                // Now the description. We also want this one to
                // fade, but the animation should also slowly move
                // down and out of the screen
                View description = page.findViewById(R.id.contentDescription);
                description.setTranslationY(-pageWidthTimesPosition / 4f);
                description.setAlpha(1.0f - absPosition);

                // Now, we want the image to move to the right,
                // i.e. in the opposite direction of the rest of the
                // content while fading out
                View computer = page.findViewById(R.id.imageView);
//                computer.setTranslationY(pageWidthTimesPosition / 4f);
//                computer.setAlpha(1.0f - absPosition);

                // We're attempting to create an effect for a View
                // specific to one of the pages in our ViewPager.
                // In other words, we need to check that we're on
                // the correct page and that the View in question
                // isn't null.
                if (computer != null) {
                    computer.setAlpha(1.0f - absPosition);
                    computer.setTranslationX(-pageWidthTimesPosition * .50f);
                }

                // Finally, it can be useful to know the direction
                // of the user's swipe - if we're entering or exiting.
                // This is quite simple:
                if (position < 0) {
                    // Create your out animation here
                } else {
                    // Create your in animation here
                }
            }
        }

    }
}

package italker.tencent.com.italkerproject.activits;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;

import italker.tencent.com.common.app.Fragment;
import italker.tencent.com.common.app.ToorbarActivity;
import italker.tencent.com.italkerproject.R;
import italker.tencent.com.italkerproject.fragments.search.SearchGroupFragment;
import italker.tencent.com.italkerproject.fragments.search.SearchUserFragment;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;



public class SearchActivity extends ToorbarActivity {

    private static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final int TYPE_USER = 1; // 搜索人
    public static final int TYPE_GROUP = 2; // 搜索群

    public SearchFragment mSearchFragment;   //接口，順便拿來進行一個賦值操作

    public int type;
    /**
     * 显示搜索界面
     *
     * @param context 上下文
     * @param type    显示的类型，用户还是群
     */
    public static void show(Context context, int type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    /**
     * 添加兩個佈局，有關於群的和聯繫人的
     */
    @Override
    protected void initWeiget() {
        super.initWeiget();
        Fragment fragment;
        if (type == TYPE_USER) {
            SearchUserFragment userFragment = new SearchUserFragment();
            fragment = userFragment;
            mSearchFragment = userFragment;
        }else{
            SearchGroupFragment groupFragment = new SearchGroupFragment();
            fragment = groupFragment;
            mSearchFragment = groupFragment;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.lay_container,fragment).commit();
    }

    @Override
    protected boolean isArgs(Bundle bundle) {
        type = bundle.getInt(EXTRA_TYPE);
        // 是搜索人或者搜索群
        return type == TYPE_USER || type == TYPE_GROUP;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 初始化菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_items, menu);

        // 找到搜索菜单
        MenuItem searchItem = menu.findItem(R.id.activity_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            // 拿到一个搜索管理器
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            // 添加搜索监听
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // 当点击了提交按钮的时候
                    search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    // 当文字改变的时候，咱们不会及时搜索，只在为null的情况下进行搜索
                    if (TextUtils.isEmpty(s)) {
                        search("");
                        return true;
                    }
                    return false;
                }
            });


        }
        return super.onCreateOptionsMenu(menu);
    }

    public void search(String name){
        if(mSearchFragment != null){
            mSearchFragment.search(name);
        }
    }

    /**
     * 搜索的Fragment必须继承的接口
     */
    public interface SearchFragment {
        void search(String content);
    }
}

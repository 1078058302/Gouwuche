package soexample.umeng.com.gouwuche;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String url = "http://www.zhaoapi.cn/product/getCarts?uid=71";
    private RecyclerView recyclerView;
    private List<ShopBean.DataBean> data1 = new ArrayList<>();
    private CheckBox checkBox;
    private boolean b = true;
    private MyAdapter1 myAdapter1;
    private TextView price;
    private MyAdapter myAdapter;
    private Button finish;
    private boolean status1;
    private double p;
    private int n;
    private double v;
    private double v2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler1);
        checkBox = findViewById(R.id.all);
        price = findViewById(R.id.price);
        finish = findViewById(R.id.finish);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //全选
                if (b) {
                    allCheck(true);
                    b = false;
                } else {
                    allCheck(false);
                    b = true;
                }

            }
        });

        doHttp();
    }

    //全选
    private void allCheck(boolean b) {
        p = 0;
        n = 0;
        for (int i = 0; i < data1.size(); i++) {
            List<ShopBean.DataBean.ListBean> listChild = data1.get(i).getList();
            for (int j = 0; j < listChild.size(); j++) {
                listChild.get(j).setStatus(b);
                status1 = listChild.get(j).isStatus();

                double price = listChild.get(j).getPrice();
                int num = listChild.get(j).getNum();
                double v = num * price;
                p += v;
                n += num;
            }
        }

        if (b) {
            price.setText(Double.toString(p));
            finish.setText("结算(" + n + ")");
        } else {
            price.setText(Double.toString(0));
            finish.setText("结算(" + 0 + ")");
        }

        myAdapter.notifyDataSetChanged();
    }

    private void doHttp() {
        new HttpHelper().get(url).result(new HttpHelper.HelperListener() {
            @Override
            public void success(String data) {
                ShopBean shopBean = new Gson().fromJson(data, ShopBean.class);
                data1 = shopBean.getData();
                data1.remove(0);
//                Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                myAdapter = new MyAdapter();
//                StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void error() {

            }
        });
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        @NonNull
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = View.inflate(MainActivity.this, R.layout.recycler_item, null);
            ViewHolder holder = new ViewHolder(view);
            holder.shopname = view.findViewById(R.id.shopname);
            holder.recyclerView = view.findViewById(R.id.recycler2);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.ViewHolder viewHolder, int i) {
            viewHolder.shopname.setText(data1.get(i).getSellerName());
            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            viewHolder.recyclerView.setLayoutManager(manager);
            myAdapter1 = new MyAdapter1(data1.get(i).getList());
            viewHolder.recyclerView.setAdapter(myAdapter1);
        }

        @Override
        public int getItemCount() {
            return data1.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            TextView shopname;
            RecyclerView recyclerView;
        }
    }

    class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.ViewHolder> {
        List<ShopBean.DataBean.ListBean> list = new ArrayList<>();

        public MyAdapter1(List<ShopBean.DataBean.ListBean> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = View.inflate(MainActivity.this, R.layout.layout_item, null);
            ViewHolder holder = new ViewHolder(view);
            holder.imageView = view.findViewById(R.id.image);
            holder.title = view.findViewById(R.id.title);
            holder.desc = view.findViewById(R.id.desc);
            holder.jiSuanView = view.findViewById(R.id.jisuanview);
            holder.price = view.findViewById(R.id.price);
            holder.checkBox = view.findViewById(R.id.cb);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyAdapter1.ViewHolder viewHolder, final int i) {
            viewHolder.title.setText(list.get(i).getTitle());
            viewHolder.desc.setText(list.get(i).getSubhead());
            int num = list.get(i).getNum();
            final double price = list.get(i).getPrice();
            viewHolder.jiSuanView.setText(num, price);
            viewHolder.price.setText(String.valueOf(list.get(i).getPrice()));
            String images = list.get(i).getImages();
            String[] split = images.split("[|]");
            if (images != null) {
                Picasso.with(MainActivity.this).load(split[0]).fit().into(viewHolder.imageView);
            } else {
                viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
            }
            viewHolder.checkBox.setChecked(list.get(i).isStatus());
            final boolean status = list.get(i).isStatus();
            viewHolder.jiSuanView.result(new JiSuanView.jisuan() {


                @Override
                public void add(int i1, double v1, int i2) {
                    Toast.makeText(MainActivity.this, i + "" + v1 + "" + i1, Toast.LENGTH_SHORT).show();
                    list.get(i).setNum(i1);

                    n += i2;
                    p += v1;
                    if (status) {
                        MainActivity.this.price.setText(Double.toString(p));
                        finish.setText("结算(" + n + ")");
                    }else{
                        return;
                    }

                }

                @Override
                public void edit(String text) {
                    
                }

                @Override
                public void jian(int i1, double v1, int i2) {
                    list.get(i).setNum(i1);
                    n += i2;
                    p += v1;
//                    MainActivity.this.price.setText(Double.toString(p));
//                    finish.setText("结算(" + n + ")");
                    if (status) {
                        MainActivity.this.price.setText(Double.toString(p));
                        finish.setText("结算(" + n + ")");
                    }else{
                        return;
                    }
                }
            });
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean status = list.get(i).isStatus();
                    if (viewHolder.checkBox.isChecked()) {
                        int num = list.get(i).getNum();
                        n += num;
                        double price1 = list.get(i).getPrice();
                        double v1 = price1 * num;
                        p += v1;
                        MainActivity.this.price.setText(Double.toString(p));
                        finish.setText("结算(" + n + ")");
                    } else {
                        int num = list.get(i).getNum();
                        n -= num;
                        double price = list.get(i).getPrice();
                        double v1 = price * num;
                        p -= v1;
                        MainActivity.this.price.setText(Double.toString(p));
                        finish.setText("结算(" + n + ")");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            ImageView imageView;
            TextView title;
            TextView price;
            TextView desc;
            CheckBox checkBox;
            JiSuanView jiSuanView;
        }

    }


}

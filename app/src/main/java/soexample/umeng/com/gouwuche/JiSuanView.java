package soexample.umeng.com.gouwuche;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class JiSuanView extends RelativeLayout {
    private int text;
    private EditText jisuan;
    private TextView add;
    private TextView jian;
    private int i = 0;
    private int x = 0;

    public JiSuanView(Context context) {
        super(context);
        init(context);
    }

    public JiSuanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public JiSuanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Context context;

    private void init(Context context) {
        this.context = context;
        View view = View.inflate(context, R.layout.jisuanview, null);
        add = view.findViewById(R.id.add);
        jian = view.findViewById(R.id.jian);
        jisuan = view.findViewById(R.id.edit_jisuan);
        

        addView(view);
    }

    public void setText(int text, double price) {
        jisuan.setText(String.valueOf(text));
        setTextShow(text, price);
    }


    public void setTextShow(int text, final double price) {
        this.text = text;
        final int[] t = {text};
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                t[0] += 1;
                i = 1;
                jisuan.setText(String.valueOf(t[0]));
                double v1 = price * i;
                js.add(t[0], v1, i);

            }
        });
        jian.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                t[0] -= 1;
                if (t[0] == 0) {
                    Toast.makeText(context, "已经是最后一件了", Toast.LENGTH_SHORT).show();
                    return;
                }
                x = -1;
                jisuan.setText(String.valueOf(t[0]));
                double v1 = price * x;
                js.jian(t[0], v1, x);
            }
        });
    }

    private jisuan js;

    public void result(jisuan js) {
        this.js = js;
    }

    public interface jisuan {
        void add(int num, double v1, int i);

        void edit(String text);

        void jian(int i, double v1, int i1);
    }
}

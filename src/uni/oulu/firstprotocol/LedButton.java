package uni.oulu.firstprotocol;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.view.View.OnClickListener;

public class LedButton extends ImageButton implements OnClickListener{

	public static final int GREY = 0;
	public static final int GREEN = 2;
	public static final int RED = 1;
	public static final int YELLOW = 3;
	
	private int [] led_states = {GREY,RED,GREEN,YELLOW};
	private int led_state = GREY;
	
	public LedButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnClickListener(this);
		setImageResource(R.drawable.grey);
	}

	public LedButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
		setImageResource(R.drawable.grey);
	}
	
	public LedButton(Context context) {
		super(context);
		setOnClickListener(this);
		setImageResource(R.drawable.grey);
	}
	
	@Override
    public void onClick(View v) {
        led_state = ((led_state+1)%led_states.length);
        switch(led_state) {
        case GREY:
        	setImageResource(R.drawable.grey);
        	break;
        case GREEN:
        	setImageResource(R.drawable.green);
        	break;
        case RED:
        	setImageResource(R.drawable.red);
        	break;
        case YELLOW:
        	setImageResource(R.drawable.yellow);
        	break;
        default:
        	break;
        }
        
        invalidate();
    }

	public int getLedState() {
		return led_state;
	}
	
}

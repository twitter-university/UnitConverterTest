package com.marakana.android.unitconverter.test;

import junit.framework.Assert;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.marakana.android.unitconverter.R;
import com.marakana.android.unitconverter.UnitConversionCategory;
import com.marakana.android.unitconverter.UnitConverterActivity;

public class UnitConverterActivityTest extends
		ActivityInstrumentationTestCase2<UnitConverterActivity> {
	private UnitConverterActivity activity;
	private Spinner category;
	private Spinner inputUnit;
	private Spinner outputUnit;
	private EditText inputAmount;
	private TextView outputAmount;
	private View reverseUnits;
	private View clearInput;
	private View copyResult;

	public UnitConverterActivityTest() {
		super("com.marakana.android.unitconverter", UnitConverterActivity.class);
	}

	private void loadActivity() {
		this.activity = super.getActivity();
		this.category = (Spinner) this.activity.findViewById(R.id.category);
		this.inputAmount = (EditText) this.activity
				.findViewById(R.id.input_amount);
		this.inputUnit = (Spinner) this.activity.findViewById(R.id.input_unit);
		this.outputAmount = (TextView) this.activity
				.findViewById(R.id.output_amount);
		this.outputUnit = (Spinner) this.activity
				.findViewById(R.id.output_unit);
		this.reverseUnits = this.activity.findViewById(R.id.reverse_units);
		this.clearInput = this.activity.findViewById(R.id.clear_input);
		this.copyResult = this.activity.findViewById(R.id.copy_result);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		super.setActivityInitialTouchMode(false);
		this.loadActivity();
	}

	public void testPreConditions() {
	    Assert.assertNotNull(this.category);
	    Assert.assertNotNull(this.category.getOnItemSelectedListener());
	    Assert.assertEquals(UnitConversionCategory.values().length,
				this.category.getAdapter().getCount());

		Assert.assertNotNull(this.inputUnit);
		Assert.assertNotNull(this.inputUnit.getOnItemSelectedListener());
		Assert.assertTrue(this.inputUnit.getAdapter().getCount() > 0);

		Assert.assertNotNull(this.outputUnit);
		Assert.assertNotNull(this.outputUnit.getOnItemSelectedListener());
		Assert.assertTrue(this.outputUnit.getAdapter().getCount() > 0);

		Assert.assertNotNull(this.inputAmount);
		Assert.assertEquals("1", this.inputAmount.getText().toString());

		Assert.assertNotNull(this.outputAmount);
		Assert.assertEquals("1", this.outputAmount.getText().toString());

		Assert.assertNotNull(this.reverseUnits);
		Assert.assertNotNull(this.clearInput);
		Assert.assertNotNull(this.copyResult);
	}

	public void testViewsVisible() {
		View rootView = this.activity.getCurrentFocus().getRootView();
		ViewAsserts.assertOnScreen(rootView, this.inputUnit);
		ViewAsserts.assertOnScreen(rootView, this.outputUnit);
		ViewAsserts.assertOnScreen(rootView, this.inputAmount);
		ViewAsserts.assertOnScreen(rootView, this.outputAmount);
		ViewAsserts.assertOnScreen(rootView, this.reverseUnits);
		ViewAsserts.assertOnScreen(rootView, this.clearInput);
		ViewAsserts.assertOnScreen(rootView, this.copyResult);
	}

	@UiThreadTest
	public void testSaveInstanceState() {
		this.category.setSelection(1);
		this.inputUnit.setSelection(2);
		this.outputUnit.setSelection(3);
		this.inputAmount.setText("123");
		String result = this.outputAmount.getText().toString();

		this.activity.finish();
		this.loadActivity();

		Assert.assertEquals(1, this.category.getSelectedItemPosition());
		Assert.assertEquals(2, this.inputUnit.getSelectedItemPosition());
		Assert.assertEquals(3, this.outputUnit.getSelectedItemPosition());
		Assert.assertEquals("123", this.inputAmount.getText().toString());
		Assert.assertEquals(result, this.outputAmount.getText().toString());
	}

	public void testCopyResult() {
		ClipboardManager clipboard = (ClipboardManager) activity
				.getSystemService(Context.CLIPBOARD_SERVICE);
		CharSequence current = clipboard.getText();
		try {
			clipboard.setText("12345");
			TouchUtils.clickView(this, this.copyResult);
			Assert.assertEquals("1", clipboard.getText());
		} finally {
			clipboard.setText(current);
		}
	}

	public void testSameConversion() {
		TouchUtils.clickView(this, this.clearInput);
		TouchUtils.tapView(this, this.inputAmount);
		super.sendKeys(KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3);
		String result =  this.outputAmount.getText().toString();
		Log.d("###testSameConversion", result);
        Assert.assertEquals("112233", result);  // keypresses are duplicated!
	}
}

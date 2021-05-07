package de.verschwiegener.atero.ui.clickgui.component.components;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.ParseException;

import org.lwjgl.input.Mouse;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.design.font.Fontrenderer;
import de.verschwiegener.atero.settings.SettingsItem;
import de.verschwiegener.atero.ui.clickgui.component.Component;
import de.verschwiegener.atero.ui.clickgui.component.PanelExtendet;
import de.verschwiegener.atero.util.render.RenderUtil;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.util.MathHelper;

public class ComponentSlider extends Component {
    private boolean sliderSelected;
    private boolean textFieldSelected;
    private int textFieldEditPosition;
    private final Fontrenderer fontRenderer;
    private final DecimalFormat df = new DecimalFormat("0.0");
    private int count;
    private boolean draw;
    private String currentValue;
    private long prev;
    private boolean buttonleft;

    public ComponentSlider(String name, int y, PanelExtendet pe) {
	super(name, y, pe);
	this.fontRenderer = Management.instance.fontrenderer;
	currentValue = df.format(getItem().getCurrentValue());
    }

    @Override
    public void drawComponent(int x, int y) {
	super.drawComponent(x, y);
	if(buttonleft) {
	    if(prev - System.currentTimeMillis() <= -500) {
	    }
	}
	if (!isParentextendet()) {
	    double percent = (getItem().getCurrentValue() - getItem().getMinValue())
		    / (getItem().getMaxValue() - getItem().getMinValue());
	    fontRenderer.drawString(getName(), (getComponentX() + 3) * 2,
		    getComponentY() * 2 - getPanelExtendet().getPanel().getPanelYOffset(), Color.white.getRGB());
	    if (textFieldSelected) {
		try {
		    int textX = (getPanelExtendet().getPanel().getX() + (getPanelExtendet().getWidth() * 2)) * 2 - 39;
		    String split1 = currentValue.substring(0, textFieldEditPosition);
		    String split2 = currentValue.substring(textFieldEditPosition, currentValue.length());
		    
		    //System.out.println("TextX: " + textX);
		    
		    fontRenderer.drawString(split1, textX,
			    (getComponentY() * 2) - getPanelExtendet().getPanel().getPanelYOffset(),
			    Color.WHITE.getRGB());
		    if(textFieldEditPosition != 0) {
			fontRenderer.drawString(split2, textX + fontRenderer.getStringWidth(split1) + 3,
				    (getComponentY() * 2) - getPanelExtendet().getPanel().getPanelYOffset(),
				    Color.WHITE.getRGB());
		    }else {
			fontRenderer.drawString(split2, textX + fontRenderer.getStringWidth(split1),
				    (getComponentY() * 2) - getPanelExtendet().getPanel().getPanelYOffset(),
				    Color.WHITE.getRGB());
		    }
		    
		    drawSelected((getComponentX() + getPanelExtendet().getWidth() - 20)
			    + fontRenderer.getStringWidth2(split1));
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    } else {
		fontRenderer.drawString(currentValue,
			(getPanelExtendet().getPanel().getX() + (getPanelExtendet().getWidth() * 2)) * 2 - 40,
			(getComponentY() * 2) - getPanelExtendet().getPanel().getPanelYOffset(), Color.WHITE.getRGB());
	    }

	    RenderUtil.fillRect(getComponentX() + 1, (getComponentY()) + 5, getPanelExtendet().getWidth(), 1.5D,
		    Management.instance.colorGray);
	    RenderUtil.fillRect(getComponentX() + 1, (getComponentY()) + 5, percent * getPanelExtendet().getWidth(),
		    1.5D, Management.instance.colorBlue);
	}
	if (sliderSelected) {
	    if (isSliderHoveredNoneY(x, y)) {
		getItem().setCurrentValue(getSliderValue(x, y));
		currentValue = df.format(getItem().getCurrentValue());
	    }
	}
    }

    private void drawSelected(int x) {
	count++;
	if (count % 200 == 0) {
	    draw = !draw;
	    count = 0;
	}
	if(draw) {
	    RenderUtil.fillRect(x + 1, getComponentY() - fontRenderer.getBaseStringHeight() + 6, 0.5, fontRenderer.getBaseStringHeight(), Color.WHITE);
	}
    }

    public void onMouseClicked(int mouseX, int mouseY, int button) {
	super.onMouseClicked(mouseX, mouseY, button);
	sliderSelected = false;
	textFieldSelected = false;
	if (!isParentextendet()) {
	    if (button == 0) {
		prev = System.currentTimeMillis();
		buttonleft = true;
		if (isSliderHovered(mouseX, mouseY)) {
		    sliderSelected = true;
		    currentValue = df.format(getItem().getCurrentValue());
		}
		if (isTextFieldHovered(mouseX, mouseY)) {
		    textFieldSelected = true;
		    draw = true;
		}

	    }
	}
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
	super.onKeyTyped(typedChar, keyCode);
	if (textFieldSelected) {
	    if (isInt(String.valueOf(typedChar))) {
		try {
		    String beforadd = currentValue.substring(0, textFieldEditPosition);
		    String afteradd = currentValue.substring(textFieldEditPosition, currentValue.length());
		    currentValue = beforadd + String.valueOf(typedChar) + afteradd;
		    textFieldEditPosition += 1;
		}catch(Exception e) {
		}
		
	    }
	    switch (keyCode) {
	    case 12:
		if(textFieldEditPosition == 0 && !currentValue.startsWith("-")) {
		    currentValue = "-" + currentValue;
		    textFieldEditPosition = 1;
		}
		break;
	    case 74:
		if(textFieldEditPosition == 0 && !currentValue.startsWith("-")) {
		    currentValue = "-" + currentValue;
		    textFieldEditPosition = 1;
		}
		break;
	    case 203:
		if (textFieldEditPosition != 0 && textFieldEditPosition > 0) {
		    textFieldEditPosition -= 1;
		}
		break;
	    case 205:
		if (textFieldEditPosition != currentValue.length() && textFieldEditPosition < currentValue.length()) {
		    textFieldEditPosition += 1;
		}
		break;
	    case 14:
		if (textFieldEditPosition - 1 > -1) {
		    try {
			String beforRemove = currentValue.substring(0, textFieldEditPosition - 1);
			String afterRemove = currentValue.substring(textFieldEditPosition, currentValue.length());
			textFieldEditPosition -= 1;
			currentValue = beforRemove + afterRemove;
		    } catch (Exception e) {
		    }

		}
		break;
	    case 28:
		try {
		    DecimalFormat f = new DecimalFormat("#0.0");
		    float value2 = df.parse(currentValue).floatValue();
		    currentValue = f.format(value2);
		    textFieldEditPosition = 0;
		    Number finalvalue = f.parse(f.format(value2));
		    if (!(finalvalue.floatValue() > getItem().getMaxValue())
			    && !(finalvalue.floatValue() < getItem().getMinValue())) {
			getItem().setCurrentValue(finalvalue.floatValue());
			textFieldSelected = false;
		    } else {
			currentValue = df.format(getItem().getCurrentValue());
		    }
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		break;
	    case 51:
		String beforsplit = currentValue.substring(0, textFieldEditPosition);
		String aftersplit = currentValue.substring(textFieldEditPosition, currentValue.length());
		currentValue = beforsplit + "," + aftersplit;
		textFieldEditPosition += 1;
		break;
	    }
	}
    }

    private boolean isTextFieldHovered(int mouseX, int mouseY) {
	return mouseX > ((getPanelExtendet().getPanel().getX() + (getPanelExtendet().getWidth() * 2)) - 20)
		&& mouseX < (getPanelExtendet().getPanel().getX() + (getPanelExtendet().getWidth() * 2))
		&& mouseY > getComponentY() - 9 && mouseY < getComponentY() + 3;
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int state) {
	super.onMouseReleased(mouseX, mouseY, state);
	if(state == 0) {
	    buttonleft = false;
	}
	sliderSelected = false;
    }

    private boolean isSliderHoveredNoneY(int mouseX, int mouseY) {
	return mouseX > (getPanelExtendet().getPanel().getX() + 1)
		&& mouseX < (getPanelExtendet().getPanel().getX() + (getPanelExtendet().getWidth() * 2) + 1);
    }

    private float getSliderValue(int mouseX, int mouseY) {
	double dif = getItem().getMaxValue() - getItem().getMinValue();
	double x = (mouseX - (getComponentX() + 1));
	double value = getItem().getMinValue()
		+ MathHelper.clamp_double(x / getPanelExtendet().getWidth(), 0.0D, 1.0D) * dif;
	return (float) value;
    }

    private boolean isSliderHovered(int mouseX, int mouseY) {
	return mouseX > (getPanelExtendet().getPanel().getX() + 1)
		&& mouseX < (getPanelExtendet().getPanel().getX() + (getPanelExtendet().getWidth() * 2) + 1)
		&& mouseY > (getComponentY() + 4.5) && mouseY < (getComponentY() + 7);
    }

    private boolean isInt(String str) {
	try {
	    int x = Integer.parseInt(str);
	    return true;
	} catch (NumberFormatException e) {
	    return false;
	}

    }

}

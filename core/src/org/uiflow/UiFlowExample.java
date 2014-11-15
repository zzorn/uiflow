package org.uiflow;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import org.uiflow.propertyeditor.model.Bean;
import org.uiflow.propertyeditor.model.BeanListener;
import org.uiflow.propertyeditor.model.Property;
import org.uiflow.propertyeditor.model.dynamic.DynamicBean;
import org.uiflow.propertyeditor.model.dynamic.DynamicProperty;
import org.uiflow.propertyeditor.model.editorconfigurations.NumberEditorConfiguration;
import org.uiflow.propertyeditor.model.editorconfigurations.TextEditorConfiguration;
import org.uiflow.propertyeditor.ui.BeanEditor;
import org.uiflow.propertyeditor.ui.LabelLocation;

public class UiFlowExample extends ApplicationAdapter {

    private Stage stage;
    private Skin skin;
    private UiContext uiContext;

    @Override
	public void create () {
        // Setup
        stage = new Stage();
        // skin = createTestSkin();
        Gdx.input.setInputProcessor(stage);
        uiContext = new DefaultUiContext();

        // Create root table
        Table rootTable = new Table();

        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        // Create UI elements
        final Bean testBean = createTestBean();
        BeanEditor beanEditor = new BeanEditor(LabelLocation.ABOVE);
        beanEditor.setBean(testBean);
        rootTable.add(beanEditor.getUi(uiContext));

        // Print changes
        testBean.addListener(createdebugPrintListener());
	}

    @Override
	public void render () {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
	}

    @Override
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void dispose () {
        stage.dispose();

        uiContext.dispose();

       // skin.dispose();
    }

    private Bean createTestBean() {
        final DynamicBean testBean = new DynamicBean("Troll");
        testBean.addProperty(new DynamicProperty("Name", TextEditorConfiguration.DEFAULT, "Igor"));
        testBean.addProperty(new DynamicProperty("Hitpoints", NumberEditorConfiguration.DOUBLE_DEFAULT, 13));
        testBean.addProperty(new DynamicProperty("Favourite Food",
                                                 TextEditorConfiguration.DEFAULT_MULTILINE,
                                                 "Tasty Hobbitses\nMushroom Soup\nCrunchy Crabs"));

        return testBean;
    }

    private Skin createTestSkin() {
        Skin skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        skin.add("default", new BitmapFont());

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        // Configure a default text field type
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        textFieldStyle.cursor = skin.newDrawable("white", Color.LIGHT_GRAY);
        textFieldStyle.disabledBackground = skin.newDrawable("white", Color.DARK_GRAY);
        textFieldStyle.disabledFontColor = Color.LIGHT_GRAY;
        textFieldStyle.focusedBackground = skin.newDrawable("white", Color.OLIVE);
        textFieldStyle.focusedFontColor = Color.WHITE;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.selection = skin.newDrawable("white", Color.BLUE);
        textFieldStyle.font = skin.getFont("default");
        skin.add("default", textFieldStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.background = skin.newDrawable("white", new Color(0.4f, 0.4f, 0.4f, 1));
        labelStyle.font = skin.getFont("default");
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        return skin;
    }

    private BeanListener createdebugPrintListener() {
        return new BeanListener() {
            @Override public void onValueChanged(Bean bean, Property property, Object newValue) {
                System.out.println("UiFlowExample.onValueChanged");
                System.out.println("bean = " + bean);
                System.out.println("property = " + property.getName());
                System.out.println("newValue = " + newValue);
            }

            @Override public void onValueEditorChanged(Bean bean, Property property) {
                System.out.println("UiFlowExample.onValueEditorChanged");
                System.out.println("bean = " + bean);
                System.out.println("property = " + property);
            }

            @Override public void onPropertyChanged(Bean bean, Property property) {
                System.out.println("UiFlowExample.onPropertyChanged");
                System.out.println("bean = " + bean);
                System.out.println("property = " + property);
            }

            @Override public void onChanged(Bean bean) {
                System.out.println("UiFlowExample.onChanged");
                System.out.println("bean = " + bean);
            }

            @Override public void onPropertyAdded(Bean bean, Property property) {
                System.out.println("UiFlowExample.onPropertyAdded");
                System.out.println("bean = " + bean);
                System.out.println("property = " + property);
            }

            @Override public void onPropertyRemoved(Bean bean, Property property) {
                System.out.println("UiFlowExample.onPropertyRemoved");
                System.out.println("bean = " + bean);
                System.out.println("property = " + property);
            }
        };
    }
}

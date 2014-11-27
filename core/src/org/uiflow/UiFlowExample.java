package org.uiflow;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.uiflow.propertyeditor.model.bean.Bean;
import org.uiflow.propertyeditor.model.bean.BeanListener;
import org.uiflow.propertyeditor.model.bean.Property;
import org.uiflow.propertyeditor.model.bean.PropertyDirection;
import org.uiflow.propertyeditor.model.bean.dynamic.DynamicBean;
import org.uiflow.propertyeditor.model.beangraph.BeanGraph;
import org.uiflow.propertyeditor.model.beangraph.DefaultBeanGraph;
import org.uiflow.propertyeditor.ui.editors.bean.LabelLocation;
import org.uiflow.propertyeditor.ui.editors.beangraph.BeanGraphEditor;
import org.uiflow.propertyeditor.ui.editors.number.NumberEditorConfiguration;
import org.uiflow.utils.colorfunction.ColorGradient;

public class UiFlowExample extends ApplicationAdapter {

    private Stage stage;
    private UiContext uiContext;

    @Override
	public void create () {
        // Setup
        stage = new Stage();

        /*
        stage.addListener(new EventListener() {
            @Override public boolean handle(Event event) {
                System.out.println("UiFlowExample.handle");
                System.out.println("event = " + event);
                return false;
            }
        });
        */

        // skin = createTestSkin();
        Gdx.input.setInputProcessor(stage);
        uiContext = new DefaultUiContext(stage);

        // Create root table
        Table rootTable = new Table(uiContext.getSkin());
        rootTable.setBackground("frame");

        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        // Create UI elements
        /*
        final Bean testBean = createTestBean();
        BeanEditor beanEditor = new BeanEditor(LabelLocation.LEFT);
        beanEditor.setValue(testBean);
        rootTable.add(beanEditor.getUi(uiContext));

        // Print changes
        testBean.addListener(createdebugPrintListener());
        */

        // Test bean graph
        BeanGraph beanGraph = new DefaultBeanGraph("Test Graph");
        beanGraph.addBean(createTestBean(), 40, 40);
        beanGraph.addBean(createTestBean(), 340, 340);
        beanGraph.addBean(createTestBean(), 740, 340);
        beanGraph.getInterfaceBean().addDouble("x", 0, PropertyDirection.IN);
        beanGraph.getInterfaceBean().addDouble("y", 0, PropertyDirection.IN);
        beanGraph.getInterfaceBean().addDouble("result", 0, PropertyDirection.OUT);
        beanGraph.getInterfaceBean().addDouble("tuning", 3.14, PropertyDirection.INOUT);
        BeanGraphEditor beanGraphEditor = new BeanGraphEditor();
        beanGraphEditor.setValue(beanGraph);
        rootTable.add(beanGraphEditor.getUi(uiContext)).fill().expand();

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
        testBean.addString("Name", "Igor");
        testBean.addDouble("Hitpoints", 24, 0, 1000, false, true, ColorGradient.RED_YELLOW);
        testBean.addDouble("Balance", 24, -100, 0, 100, false, true, ColorGradient.RED_GREEN_RED);
        testBean.addDouble("Balance2", 12, -10000, 0, 10000, true, true, ColorGradient.RED_GREEN_RED
        );
        testBean.addInt("Inventory Slots", 16, 0, 100, false, false);
        testBean.addString("Favourite Foods", "Tasty Hobbitses\nMushroom Soup\nCrunchy Crabs", 4);

        DynamicBean appearance = new DynamicBean("Appearance");
        appearance.addString("Hat", "Top Hat");
        appearance.addDouble("Height", 5, 0, 10, true, false, ColorGradient.BLUE_RED);
        appearance.addString("Color");
        testBean.addBean("Appearance", appearance, LabelLocation.LEFT);

        return testBean;
    }


    private BeanListener createdebugPrintListener() {
        return new BeanListener() {
            @Override public void onBeanNameChanged(Bean bean) {
                System.out.println("onBeanNameChanged");
                System.out.println("  bean = " + bean);
            }

            @Override public void onValueChanged(Bean bean, Property property, Object newValue) {
                System.out.println("onValueChanged");
                System.out.println("  bean = " + bean);
                System.out.println("  property = " + property.getName());
                System.out.println("  newValue = " + newValue);
            }

            @Override public void onValueEditorChanged(Bean bean, Property property) {
                System.out.println("onValueEditorChanged");
                System.out.println("  bean = " + bean);
                System.out.println("  property = " + property);
            }

            @Override public void onPropertyChanged(Bean bean, Property property) {
                System.out.println("onPropertyChanged");
                System.out.println("  bean = " + bean);
                System.out.println("  property = " + property);
            }

            @Override public void onPropertyAdded(Bean bean, Property property) {
                System.out.println("onPropertyAdded");
                System.out.println("  bean = " + bean);
                System.out.println("  property = " + property);
            }

            @Override public void onPropertyRemoved(Bean bean, Property property) {
                System.out.println("onPropertyRemoved");
                System.out.println("  bean = " + bean);
                System.out.println("  property = " + property);
            }

            @Override public void onSourceChanged(Bean bean, Property property, Property newSource) {
                System.out.println("onSourceChanged");
                System.out.println("  bean = " + bean);
                System.out.println("  property = " + property);
                System.out.println("  newSource = " + newSource);
            }
        };
    }
}

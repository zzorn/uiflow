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
import org.uiflow.propertyeditor.model.bean.dynamic.DynamicProperty;
import org.uiflow.propertyeditor.model.beangraph.BeanGraph;
import org.uiflow.propertyeditor.model.beangraph.DefaultBeanGraph;
import org.uiflow.propertyeditor.ui.editors.bean.LabelLocation;
import org.uiflow.propertyeditor.ui.editors.beangraph.BeanGraphConfiguration;
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
        testBean.addListener(createDebugPrintListener());
        */

        // Test bean graph
        BeanGraph beanGraph = new DefaultBeanGraph("Test Graph");
//        beanGraph.addBean(createTestBean(), 40, 40);
        final Bean beanA = beanGraph.addBean(createTestBean(), 340, 340);
        final Bean beanB = beanGraph.addBean(createTestBean(), 740, 340);
        final Property x = beanGraph.getInterfaceBean().addFloat("x", 0, PropertyDirection.IN);
        final Property y = beanGraph.getInterfaceBean().addFloat("y", 0, PropertyDirection.IN);
        final Property result = beanGraph.getInterfaceBean().addDouble("result", 0, PropertyDirection.OUT);
        beanGraph.getInterfaceBean().addFloat("tuning", 3.14f, PropertyDirection.INOUT);
        BeanGraphEditor beanGraphEditor = new BeanGraphEditor();
        beanGraphEditor.setValue(beanGraph);
        rootTable.add(beanGraphEditor.getUi(uiContext)).fill().expand();

        beanA.getProperty("Balance").setSource(x);
        beanA.getProperty("Balance2").set(y);
        beanB.getProperty("Hitpoints").setSource(beanA.getProperty("Balance"));
        beanB.getProperty("Inventory Slots").setSource(beanA.getProperty("Inventory Slots"));
        beanA.getProperty("Favourite Foods").setSource(beanB.getProperty("Name"));
        result.set(beanB.getProperty("Balance2"));

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
        testBean.addBean("Appearance", appearance, LabelLocation.LEFT);
        testBean.addString("Color", "FF00FF", 1, PropertyDirection.IN);
        testBean.addString("Favourite Saying", "Zug Zug", 1, PropertyDirection.OUT);

        /* Yes, you can add a BeanGraph as a property to a Bean, and you get an editor in the bean.  No, it's not very practical.
        final Property property = testBean.addProperty("We Have To Go Deeper",
                                                       BeanGraph.class,
                                                       new BeanGraphConfiguration(),
                                                       PropertyDirection.IN);

        final DefaultBeanGraph nestedGraph = new DefaultBeanGraph("Ho Ho Hoo");
        nestedGraph.addBean(new DynamicBean("FooFoo", new DynamicProperty("Abcdf", Double.class, new NumberEditorConfiguration())), 50, 50);
        nestedGraph.getInterfaceBean().addDouble("A");
        nestedGraph.getInterfaceBean().addDouble("B");
        property.setValue(nestedGraph);
        */

        return testBean;
    }


    private BeanListener createDebugPrintListener() {
        return new BeanListener() {
            @Override public void onBeanNameChanged(Bean bean) {
                System.out.println("onBeanNameChanged");
                System.out.println("  bean = " + bean);
            }

            @Override public void onValueChanged(Bean bean, Property property, Object oldValue, Object newValue) {
                System.out.println("onValueChanged");
                System.out.println("  bean = " + bean);
                System.out.println("  property = " + property.getName());
                System.out.println("  oldValue = " + oldValue);
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

            @Override public void onSourceChanged(Bean bean, Property property, Property oldSource, Property newSource) {
                System.out.println("onSourceChanged");
                System.out.println("  bean = " + bean);
                System.out.println("  property = " + property);
                System.out.println("  oldSource = " + oldSource);
                System.out.println("  newSource = " + newSource);
            }
        };
    }
}

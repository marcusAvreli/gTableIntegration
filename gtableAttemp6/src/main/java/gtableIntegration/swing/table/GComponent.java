package gtableIntegration.swing.table;

import javax.swing.JComponent;
import javax.swing.JPanel;

import gtableIntegration.gdev.gfld.GFormField;

//https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gawt/GComponent.java
public abstract class GComponent extends JPanel
{protected GFormField m_objFormField;
	 protected JComponent m_objComponent;
	 public GComponent(GFormField ff) {
		   setLayout(null);
	        setFormField(ff);
	 }
	  public GFormField getFormField()
	    {
	        return m_objFormField;
	    }
	   /**
	     * Cambia el campo al que referencia este componente de la interfaz
	     * @param ff Es el nuevo campo a referenciar
	     */
	    public void setFormField(GFormField ff)
	    {
	        m_objFormField = ff;
	    }
}
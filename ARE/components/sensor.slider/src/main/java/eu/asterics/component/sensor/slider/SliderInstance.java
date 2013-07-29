

/*
 *    AsTeRICS - Assistive Technology Rapid Integration and Construction Set
 * 
 * 
 *        d8888      88888888888       8888888b.  8888888 .d8888b.   .d8888b. 
 *       d88888          888           888   Y88b   888  d88P  Y88b d88P  Y88b
 *      d88P888          888           888    888   888  888    888 Y88b.     
 *     d88P 888 .d8888b  888   .d88b.  888   d88P   888  888         "Y888b.  
 *    d88P  888 88K      888  d8P  Y8b 8888888P"    888  888            "Y88b.
 *   d88P   888 "Y8888b. 888  88888888 888 T88b     888  888    888       "888
 *  d8888888888      X88 888  Y8b.     888  T88b    888  Y88b  d88P Y88b  d88P
 * d88P     888  88888P' 888   "Y8888  888   T88b 8888888 "Y8888P"   "Y8888P" 
 *
 *
 *                    homepage: http://www.asterics.org 
 *
 *    This project has been partly funded by the European Commission, 
 *                      Grant Agreement Number 247730
 *  
 *  
 *    License: GPL v3.0 (GNU General Public License Version 3.0)
 *                 http://www.gnu.org/licenses/gpl.html
 * 
 */

package eu.asterics.component.sensor.slider;


import java.util.logging.Logger;

import eu.asterics.mw.data.ConversionUtils;
import eu.asterics.mw.model.runtime.AbstractRuntimeComponentInstance;
import eu.asterics.mw.model.runtime.IRuntimeInputPort;
import eu.asterics.mw.model.runtime.IRuntimeOutputPort;
import eu.asterics.mw.model.runtime.IRuntimeEventListenerPort;
import eu.asterics.mw.model.runtime.IRuntimeEventTriggererPort;
import eu.asterics.mw.model.runtime.impl.DefaultRuntimeOutputPort;
import eu.asterics.mw.model.runtime.impl.DefaultRuntimeEventTriggererPort;
import eu.asterics.mw.services.AREServices;
import eu.asterics.mw.services.AstericsErrorHandling;

/**
 * 
 * implements the slider plugin, which provides the current slider
 * position as a double value at the output port
 * 
 * 
 *  
 * @author Chris Veigl [veigl@technikum-wien.at]
 *         Date: 11.10.2011
 *         Time: 16:25
 */
public class SliderInstance extends AbstractRuntimeComponentInstance
{
	final IRuntimeOutputPort opValue = new DefaultRuntimeOutputPort();
	// Usage of an output port e.g.: opMyOutPort.sendData(10); 

	public int propMin = 0;
	public int propMax = 100;
	public int propDefault = 50;
	public String propCaption = "my slider";
	public int propMajorTickSpacing = 20;
	public int propMinorTickSpacing = 5;
    public int propFontSize =14; 
	public int propAlignment = 0;
	public boolean propAutosend = true;

	// declare member variables here

	private  GUI gui = null;

	/**
	 * The class constructor.
	 */
	public SliderInstance()
	{
		// empty constructor
	}

	/**
	 * returns an Input Port.
	 * @param portID   the name of the port
	 * @return         the input port or null if not found
	 */
	public IRuntimeInputPort getInputPort(String portID)
	{

		return null;
	}

	/**
	 * returns an Output Port.
	 * @param portID   the name of the port
	 * @return         the output port or null if not found
	 */
	public IRuntimeOutputPort getOutputPort(String portID)
	{
		if ("value".equalsIgnoreCase(portID))
		{
			return opValue;
		}

		return null;
	}

	/**
	 * returns an Event Listener Port.
	 * @param eventPortID   the name of the port
	 * @return         the EventListener port or null if not found
	 */
	public IRuntimeEventListenerPort getEventListenerPort(String eventPortID)
	{

		return null;
	}

	/**
	 * returns an Event Triggerer Port.
	 * @param eventPortID   the name of the port
	 * @return         the EventTriggerer port or null if not found
	 */
	public IRuntimeEventTriggererPort getEventTriggererPort(String eventPortID)
	{

		return null;
	}

	/**
	 * returns the value of the given property.
	 * @param propertyName   the name of the property
	 * @return               the property value or null if not found
	 */
	public Object getRuntimePropertyValue(String propertyName)
	{
		if ("min".equalsIgnoreCase(propertyName))
		{
			return propMin;
		}
		if ("max".equalsIgnoreCase(propertyName))
		{
			return propMax;
		}
		if ("default".equalsIgnoreCase(propertyName))
		{
			return propDefault;
		}
		if("autosend".equalsIgnoreCase(propertyName))
		{
			return propAutosend;
		}
		if ("caption".equalsIgnoreCase(propertyName))
		{
			return propCaption;
		}
		if ("majorTickSpacing".equalsIgnoreCase(propertyName))
		{
			return propMajorTickSpacing;
		}
		if ("minorTickSpacing".equalsIgnoreCase(propertyName))
		{
			return propMinorTickSpacing;
		}
		if ("alignment".equalsIgnoreCase(propertyName))
		{
			return propAlignment;
		}
        if("fontSize".equalsIgnoreCase(propertyName))
        {
            return propFontSize;
        }
		return null;
	}

	/**
	 * sets a new value for the given property.
	 * @param[in] propertyName   the name of the property
	 * @param[in] newValue       the desired property value or null if not found
	 */
	public Object setRuntimePropertyValue(String propertyName, Object newValue)
	{
		if ("min".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propMin;
			propMin =  Integer.parseInt(newValue.toString());
			return oldValue;
		}
		if ("max".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propMax;
			propMax =  Integer.parseInt(newValue.toString());
			return oldValue;
		}
		if ("default".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propDefault;
			propDefault =  Integer.parseInt(newValue.toString());
			if (propDefault < propMin) propDefault = propMin;
			if (propDefault > propMax) propDefault = propMax;
			return oldValue;
		}
		if("autosend".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propAutosend;
			if("true".equalsIgnoreCase((String)newValue))
			{
				propAutosend = true;
			}
			else if("false".equalsIgnoreCase((String)newValue))
			{
				propAutosend = false;
			}
			return oldValue;
		}		
		if ("caption".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propCaption;
			propCaption = (String)newValue;
			return oldValue;
		}
		if ("majorTickSpacing".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propMajorTickSpacing;
			propMajorTickSpacing = Integer.parseInt(newValue.toString());
			return oldValue;
		}
		if ("minorTickSpacing".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propMinorTickSpacing;
			propMinorTickSpacing = Integer.parseInt(newValue.toString());
			return oldValue;
		}
		if ("alignment".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propAlignment;
			propAlignment = Integer.parseInt(newValue.toString());
			return oldValue;
		}
        if("fontSize".equalsIgnoreCase(propertyName))
        {
            final Object oldValue =  propFontSize;
            propFontSize = Integer.parseInt(newValue.toString());
            return oldValue;
        }
		return null;
	}



	/**
	 * called when model is started.
	 */
	@Override
	public void start()
	{
		if (propDefault < propMin) propDefault = propMin;
		if (propDefault > propMax) propDefault = propMax;

		gui = new GUI(this,AREServices.instance.getAvailableSpace(this));
		AREServices.instance.displayPanel(gui, this, true);
		if (propAutosend == true) opValue.sendData(ConversionUtils.intToBytes(propDefault));
		super.start();
	}

	/**
	 * called when model is paused.
	 */
	@Override
	public void pause()
	{
		super.pause();
	}

	/**
	 * called when model is resumed.
	 */
	@Override
	public void resume()
	{
		if (propDefault < propMin) propDefault = propMin;
		if (propDefault > propMax) propDefault = propMax;

		super.resume();
	}

	/**
	 * called when model is stopped.
	 */
	@Override
	public void stop()
	{
		AREServices.instance.displayPanel(gui, this, false);

		super.stop();
	}
}
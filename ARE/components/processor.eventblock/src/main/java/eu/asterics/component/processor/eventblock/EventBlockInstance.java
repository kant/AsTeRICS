

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
 *       This project has been partly funded by the European Commission, 
 *                      Grant Agreement Number 247730
 *  
 *  
 *    License: GPL v3.0 (GNU General Public License Version 3.0)
 *                 http://www.gnu.org/licenses/gpl.html
 * 
 */

package eu.asterics.component.processor.eventblock;


import java.util.logging.Logger;
import eu.asterics.mw.data.ConversionUtils;
import eu.asterics.mw.model.runtime.AbstractRuntimeComponentInstance;
import eu.asterics.mw.model.runtime.IRuntimeInputPort;
import eu.asterics.mw.model.runtime.impl.DefaultRuntimeInputPort;

import eu.asterics.mw.model.runtime.IRuntimeOutputPort;
import eu.asterics.mw.model.runtime.IRuntimeEventListenerPort;
import eu.asterics.mw.model.runtime.IRuntimeEventTriggererPort;
import eu.asterics.mw.model.runtime.impl.DefaultRuntimeOutputPort;
import eu.asterics.mw.model.runtime.impl.DefaultRuntimeEventTriggererPort;
import eu.asterics.mw.services.AstericsErrorHandling;
import eu.asterics.mw.services.AREServices;

/**
 * 
 * This plugin can be used to pass or block passing events according to the user settings.
 * 
 *  
 * @author Karol Pecyna [kpecyna@harpo.com.pl]
 *         Date: Apr 04, 2012
 *         Time: 09:32:04 AM
 */
public class EventBlockInstance extends AbstractRuntimeComponentInstance
{
	// Usage of an output port e.g.: opMyOutPort.sendData(ConversionUtils.intToBytes(10)); 

	final IRuntimeEventTriggererPort etpOutput = new DefaultRuntimeEventTriggererPort();
	// Usage of an event trigger port e.g.: etpMyEtPort.raiseEvent();

	boolean propBlock = false;
	boolean propBlockAfterEvent = false;
    private boolean pass=true;
	// declare member variables here

  
    
   /**
    * The class constructor.
    */
    public EventBlockInstance()
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

		return null;
	}

    /**
     * returns an Event Listener Port.
     * @param eventPortID   the name of the port
     * @return         the EventListener port or null if not found
     */
    public IRuntimeEventListenerPort getEventListenerPort(String eventPortID)
    {
		if ("input".equalsIgnoreCase(eventPortID))
		{
			return elpInput;
		}
		if ("pass".equalsIgnoreCase(eventPortID))
		{
			return elpPass;
		}
		if ("block".equalsIgnoreCase(eventPortID))
		{
			return elpBlock;
		}
		if ("change".equalsIgnoreCase(eventPortID))
		{
			return elpChange;
		}

        return null;
    }

    /**
     * returns an Event Triggerer Port.
     * @param eventPortID   the name of the port
     * @return         the EventTriggerer port or null if not found
     */
    public IRuntimeEventTriggererPort getEventTriggererPort(String eventPortID)
    {
		if ("output".equalsIgnoreCase(eventPortID))
		{
			return etpOutput;
		}

        return null;
    }
		
    /**
     * returns the value of the given property.
     * @param propertyName   the name of the property
     * @return               the property value or null if not found
     */
    public Object getRuntimePropertyValue(String propertyName)
    {
		if ("block".equalsIgnoreCase(propertyName))
		{
			return propBlock;
		}
		if ("blockAfterEvent".equalsIgnoreCase(propertyName))
		{
			return propBlockAfterEvent;
		}

        return null;
    }

    /**
     * sets a new value for the given property.
     * @param propertyName   the name of the property
     * @param newValue       the desired property value or null if not found
     */
    public Object setRuntimePropertyValue(String propertyName, Object newValue)
    {
		if ("block".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propBlock;
			if("true".equalsIgnoreCase((String)newValue))
			{
				propBlock = true;
				pass=false;
			}
			else if("false".equalsIgnoreCase((String)newValue))
			{
				propBlock = false;
				pass=true;
			}
			return oldValue;
		}
		if ("blockAfterEvent".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propBlockAfterEvent;
			if("true".equalsIgnoreCase((String)newValue))
			{
				propBlockAfterEvent = true;
			}
			else if("false".equalsIgnoreCase((String)newValue))
			{
				propBlockAfterEvent = false;
			}
			return oldValue;
		}

        return null;
    }

     /**
      * Input Ports for receiving values.
      */


     /**
      * Event Listerner Ports.
      */
	final IRuntimeEventListenerPort elpInput = new IRuntimeEventListenerPort()
	{
		public void receiveEvent(final String data)
		{
				 if(pass)
				 {
					 etpOutput.raiseEvent();
					 if(propBlockAfterEvent)
					 {
						 pass=false;
					 }
				 }
		}
	};
	final IRuntimeEventListenerPort elpPass = new IRuntimeEventListenerPort()
	{
		public void receiveEvent(final String data)
		{
				 pass=true; 
		}
	};
	final IRuntimeEventListenerPort elpBlock = new IRuntimeEventListenerPort()
	{
		public void receiveEvent(final String data)
		{
				 pass=false; 
		}
	};
	final IRuntimeEventListenerPort elpChange = new IRuntimeEventListenerPort()
	{
		public void receiveEvent(final String data)
		{
				 pass=!pass;
		}
	};

	

     /**
      * called when model is started.
      */
      @Override
      public void start()
      {

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
          super.resume();
      }

     /**
      * called when model is stopped.
      */
      @Override
      public void stop()
      {

          super.stop();
      }
}
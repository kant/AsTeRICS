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
 *         Dual License: MIT or GPL v3.0 with "CLASSPATH" exception
 *         (please refer to the folder LICENSE)
 * 
 */

package eu.asterics.component.actuator.skywatchermount;

import eu.asterics.mw.data.ConversionUtils;
import eu.asterics.mw.model.runtime.AbstractRuntimeComponentInstance;
import eu.asterics.mw.model.runtime.IRuntimeEventListenerPort;
import eu.asterics.mw.model.runtime.IRuntimeEventTriggererPort;
import eu.asterics.mw.model.runtime.IRuntimeInputPort;
import eu.asterics.mw.model.runtime.IRuntimeOutputPort;
import eu.asterics.mw.model.runtime.impl.DefaultRuntimeInputPort;
import eu.asterics.mw.model.runtime.impl.DefaultRuntimeOutputPort;

/**
 * 
 * This plugin is used to control telescope mounts using the Nexstar 5 protocol.
 * 
 * 
 * 
 * @author David Thaller [dt@ki-i.at] Date: 02.09.2013 Time:
 */
public class SkyWatcherMountInstance extends AbstractRuntimeComponentInstance implements PositionEventListener {
    final IRuntimeOutputPort opPosX = new DefaultRuntimeOutputPort();
    final IRuntimeOutputPort opPosY = new DefaultRuntimeOutputPort();
    // Usage of an output port e.g.:
    // opMyOutPort.sendData(ConversionUtils.intToBytes(10));

    // Usage of an event trigger port e.g.: etpMyEtPort.raiseEvent();

    String propSerialPort = "COMX";
    private SkyWatcher skyWatcher;
    private int tiltPosition;
    private int panPosition;
    // declare member variables here
    int maxLeft = 0;
    int maxRight = 0;
    int maxUp = 0;
    int maxDown = 0;
    boolean limitsActive = false;

    /**
     * The class constructor.
     */
    public SkyWatcherMountInstance() {
        // empty constructor
    }

    /**
     * returns an Input Port.
     * 
     * @param portID
     *            the name of the port
     * @return the input port or null if not found
     */
    @Override
    public IRuntimeInputPort getInputPort(String portID) {
        if ("speed".equalsIgnoreCase(portID)) {
            return ipSpeedPort;
        } else if ("tiltPosition".equalsIgnoreCase(portID)) {
            return ipGoToTiltPositionPort;
        } else if ("panPosition".equalsIgnoreCase(portID)) {
            return ipGoToPanPositionPort;
        }
        if ("endpointleft".equalsIgnoreCase(portID)) {
            return ipEndPointLeft;
        }
        if ("endpointright".equalsIgnoreCase(portID)) {
            return ipEndPointRight;
        }
        if ("endpointup".equalsIgnoreCase(portID)) {
            return ipEndPointUp;
        }
        if ("endpointdown".equalsIgnoreCase(portID)) {
            return ipEndPointDown;
        }
        return null;
    }

    /**
     * Input Port for receiving values.
     */
    private final IRuntimeInputPort ipSpeedPort = new DefaultRuntimeInputPort() {
        @Override
        public void receiveData(byte[] data) {
            if (skyWatcher != null) {
                skyWatcher.setSpeed(ConversionUtils.intFromBytes(data));
            }
        }

    };

    private final IRuntimeInputPort ipEndPointLeft = new DefaultRuntimeInputPort() {
        @Override
        public void receiveData(byte[] data) {
            if (skyWatcher != null) {
                maxLeft = ConversionUtils.intFromBytes(data);
                skyWatcher.setMaxLeft(maxLeft);
            }

        }

    };

    private final IRuntimeInputPort ipEndPointRight = new DefaultRuntimeInputPort() {
        @Override
        public void receiveData(byte[] data) {
            if (skyWatcher != null) {
                maxRight = ConversionUtils.intFromBytes(data);
                skyWatcher.setMaxRight(maxRight);
            }

        }

    };

    private final IRuntimeInputPort ipEndPointUp = new DefaultRuntimeInputPort() {
        @Override
        public void receiveData(byte[] data) {
            if (skyWatcher != null) {
                maxUp = ConversionUtils.intFromBytes(data);
                skyWatcher.setMaxUp(maxUp);
            }

        }

    };

    private final IRuntimeInputPort ipEndPointDown = new DefaultRuntimeInputPort() {
        @Override
        public void receiveData(byte[] data) {
            if (skyWatcher != null) {
                maxDown = ConversionUtils.intFromBytes(data);
                skyWatcher.setMaxDown(maxDown);
            }
        }

    };

    private final IRuntimeInputPort ipGoToPanPositionPort = new DefaultRuntimeInputPort() {
        @Override
        public void receiveData(byte[] data) {
            panPosition = ConversionUtils.intFromBytes(data);
        }

    };

    private final IRuntimeInputPort ipGoToTiltPositionPort = new DefaultRuntimeInputPort() {
        @Override
        public void receiveData(byte[] data) {
            tiltPosition = ConversionUtils.intFromBytes(data);
        }

    };

    /**
     * returns an Output Port.
     * 
     * @param portID
     *            the name of the port
     * @return the output port or null if not found
     */
    @Override
    public IRuntimeOutputPort getOutputPort(String portID) {
        if ("posX".equalsIgnoreCase(portID)) {
            return opPosX;
        }
        if ("posY".equalsIgnoreCase(portID)) {
            return opPosY;
        }

        return null;
    }

    /**
     * returns an Event Listener Port.
     * 
     * @param eventPortID
     *            the name of the port
     * @return the EventListener port or null if not found
     */
    @Override
    public IRuntimeEventListenerPort getEventListenerPort(String eventPortID) {
        if ("goLeft".equalsIgnoreCase(eventPortID)) {
            return elpGoLeft;
        }
        if ("goRandom".equalsIgnoreCase(eventPortID)) {
            return elpGoRandom;
        }
        if ("goRight".equalsIgnoreCase(eventPortID)) {
            return elpGoRight;
        }
        if ("goUp".equalsIgnoreCase(eventPortID)) {
            return elpGoUp;
        }
        if ("goDown".equalsIgnoreCase(eventPortID)) {
            return elpGoDown;
        }
        if ("stopPan".equalsIgnoreCase(eventPortID)) {
            return elpStopPan;
        }
        if ("stopTilt".equalsIgnoreCase(eventPortID)) {
            return elpStopTilt;
        }
        if ("stop".equalsIgnoreCase(eventPortID)) {
            return elpStop;
        }
        if ("goToPanPosition".equalsIgnoreCase(eventPortID)) {
            return elpGoToPanPosition;
        }
        if ("goToTiltPosition".equalsIgnoreCase(eventPortID)) {
            return elpGoToTiltPosition;
        }
        if ("triggerOn".equalsIgnoreCase(eventPortID)) {
            return elpTriggerOn;
        }
        if ("triggerOff".equalsIgnoreCase(eventPortID)) {
            return elpTriggerOff;
        }
        if ("enableLimits".equalsIgnoreCase(eventPortID)) {
            return elpEnableLimit;
        }
        if ("disableLimits".equalsIgnoreCase(eventPortID)) {
            return elpDisableLimit;
        }
        if ("enable".equalsIgnoreCase(eventPortID)) {
            return elpEnable;
        }
        if ("disable".equalsIgnoreCase(eventPortID)) {
            return elpDisable;
        }
        return null;
    }

    /**
     * returns an Event Triggerer Port.
     * 
     * @param eventPortID
     *            the name of the port
     * @return the EventTriggerer port or null if not found
     */
    @Override
    public IRuntimeEventTriggererPort getEventTriggererPort(String eventPortID) {

        return null;
    }

    /**
     * returns the value of the given property.
     * 
     * @param propertyName
     *            the name of the property
     * @return the property value or null if not found
     */
    @Override
    public Object getRuntimePropertyValue(String propertyName) {
        if ("serialPort".equalsIgnoreCase(propertyName)) {
            return propSerialPort;
        }

        return null;
    }

    /**
     * sets a new value for the given property.
     * 
     * @param propertyName
     *            the name of the property
     * @param newValue
     *            the desired property value or null if not found
     */
    @Override
    public Object setRuntimePropertyValue(String propertyName, Object newValue) {
        if ("serialPort".equalsIgnoreCase(propertyName)) {
            final Object oldValue = propSerialPort;
            propSerialPort = (String) newValue;
            return oldValue;
        }
        /*
         * if ("EndPointLeft".equalsIgnoreCase(propertyName)) { maxLeft =
         * Integer.parseInt(newValue.toString()); if (skyWatcher == null) {
         * return null; } skyWatcher.setMaxLeft(maxLeft); return null; } if
         * ("EndPointRight".equalsIgnoreCase(propertyName)) { maxRight =
         * Integer.parseInt(newValue.toString()); if (skyWatcher == null) return
         * null; skyWatcher.setMaxRight(maxRight); return null; } if
         * ("EndPointUp".equalsIgnoreCase(propertyName)) { maxUp =
         * Integer.parseInt(newValue.toString()); if (skyWatcher == null) return
         * null; skyWatcher.setMaxUp(maxUp); return null; } if
         * ("EndPointDown".equalsIgnoreCase(propertyName)) { maxDown =
         * Integer.parseInt(newValue.toString()); if (skyWatcher == null) return
         * null; skyWatcher.setMaxDown(maxDown); return null; }
         */
        if ("endPointsActive".equalsIgnoreCase(propertyName)) {
            limitsActive = newValue.toString().equalsIgnoreCase("true");
            if (skyWatcher == null) {
                return null;
            }
            final Object oldValue = propSerialPort;
            skyWatcher.setLimitActive(limitsActive);
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

    final IRuntimeEventListenerPort elpGoRandom = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher == null) {
                return;
            }

            skyWatcher.goToRandomPosition();
        }
    };

    final IRuntimeEventListenerPort elpGoLeft = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher == null) {
                return;
            }

            skyWatcher.goLeft();
        }
    };
    final IRuntimeEventListenerPort elpEnableLimit = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher == null) {
                return;
            }
            skyWatcher.setLimitActive(true);
        }
    };
    final IRuntimeEventListenerPort elpDisableLimit = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher == null) {
                return;
            }
            skyWatcher.setLimitActive(false);
        }
    };

    final IRuntimeEventListenerPort elpGoRight = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher == null) {
                return;
            }
            skyWatcher.goRight();
        }
    };
    final IRuntimeEventListenerPort elpGoUp = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher == null) {
                return;
            }
            skyWatcher.goUp();
        }
    };
    final IRuntimeEventListenerPort elpGoDown = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher == null) {
                return;
            }
            skyWatcher.goDown();
        }
    };
    final IRuntimeEventListenerPort elpStopPan = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher == null) {
                return;
            }
            skyWatcher.stopPan();
        }
    };
    final IRuntimeEventListenerPort elpStopTilt = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher == null) {
                return;
            }
            skyWatcher.stopTilt();
        }
    };
    final IRuntimeEventListenerPort elpStop = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher != null) {
                skyWatcher.stop();
            }
        }
    };

    final IRuntimeEventListenerPort elpEnable = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher != null) {
                skyWatcher.setEnabled(true);
            }
        }
    };

    final IRuntimeEventListenerPort elpDisable = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher != null) {
                skyWatcher.setEnabled(false);
            }
        }
    };

    final IRuntimeEventListenerPort elpGoToPanPosition = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher != null) {
                skyWatcher.goToPan(panPosition);
            }
        }
    };

    final IRuntimeEventListenerPort elpGoToTiltPosition = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher != null) {
                skyWatcher.goToTilt(tiltPosition);
            }
        }
    };

    final IRuntimeEventListenerPort elpTriggerOn = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher != null) {
                skyWatcher.setTrigger(true);
            }
        }
    };

    final IRuntimeEventListenerPort elpTriggerOff = new IRuntimeEventListenerPort() {
        @Override
        public synchronized void receiveEvent(final String data) {
            if (skyWatcher != null) {
                skyWatcher.setTrigger(false);
            }
        }
    };

    @Override
    public void updatePosition(int axis, int position) {
        if (axis == 1) {
            // opPosX.sendData(ByteBuffer.allocate(4).putInt(position).array());
            opPosX.sendData(ConversionUtils.doubleToBytes(position));
        } else {
            // opPosY.sendData(ByteBuffer.allocate(4).putInt(position).array());
            opPosY.sendData(ConversionUtils.doubleToBytes(position));
        }
    }

    /**
     * called when model is started.
     */
    @Override
    public void start() {
        if (skyWatcher == null) {
            skyWatcher = new SkyWatcher(propSerialPort);
            skyWatcher.addPositionListener(this);
        } else {
            skyWatcher.init();
        }
        skyWatcher.setMaxLeft(maxLeft);
        skyWatcher.setMaxRight(maxRight);
        skyWatcher.setMaxDown(maxDown);
        skyWatcher.setMaxUp(maxUp);
        skyWatcher.setLimitActive(limitsActive);
        super.start();
    }

    /**
     * called when model is paused.
     */
    @Override
    public void pause() {
        super.pause();
    }

    /**
     * called when model is resumed.
     */
    @Override
    public void resume() {
        super.resume();
    }

    /**
     * called when model is stopped.
     */
    @Override
    public void stop() {
        if (skyWatcher != null) {
            skyWatcher.removePositionListener(this);
            skyWatcher.close();
            skyWatcher = null;
        }

        super.stop();
    }
}
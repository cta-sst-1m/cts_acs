/*
 * Copyright (c) 2015, CTA collaboration. All rights reserved.
 *
 * @author vsliusar, UA
 * @author yrenier, unige
 * based on MST WeatherStation ACS Component
 */
package cta.ctsarraycontolsystemsst.CTSArrayContolSystemImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import alma.ACS.NoSuchCharacteristic;
import alma.ACS.Property;
import alma.ACS.ROboolean;
import alma.ACS.RObooleanHelper;
import alma.ACS.RObooleanPOATie;
import alma.ACS.RObooleanSeq;
import alma.ACS.RObooleanSeqOperations;
import alma.ACS.RObooleanSeqPOATie;
import alma.ACS.ROdouble;
import alma.ACS.ROdoubleHelper;
import alma.ACS.ROdoublePOATie;
import alma.ACS.ROdoubleSeq;
import alma.ACS.ROdoubleSeqHelper;
import alma.ACS.ROdoubleSeqPOATie;
import alma.ACS.ROfloat;
import alma.ACS.ROfloatHelper;
import alma.ACS.ROfloatPOATie;
import alma.ACS.ROfloatSeq;
import alma.ACS.ROlong;
import alma.ACS.ROlongHelper;
import alma.ACS.ROlongLong;
import alma.ACS.ROlongLongHelper;
import alma.ACS.ROlongLongPOATie;
import alma.ACS.ROlongPOATie;
import alma.ACS.ROlongSeq;
import alma.ACS.ROlongSeqHelper;
import alma.ACS.ROlongSeqPOATie;
import alma.ACS.ROstring;
import alma.ACS.ROstringHelper;
import alma.ACS.ROstringPOATie;
import alma.ACS.ROstringSeq;
import alma.ACS.ROstringSeqPOATie;
import alma.ACS.impl.CharacteristicComponentImpl;
import alma.ACS.impl.CommonROEnumPropertyImpl;
import alma.ACS.impl.RObooleanImpl;
import alma.ACS.impl.ROdoubleImpl;
import alma.ACS.impl.ROdoubleSeqImpl;
import alma.ACS.impl.ROfloatImpl;
import alma.ACS.impl.ROlongImpl;
import alma.ACS.impl.ROlongLongImpl;
import alma.ACS.impl.ROlongSeqImpl;
import alma.ACS.impl.ROstringImpl;
import alma.ACS.impl.ROstringSeqImpl;
import alma.ACS.jbaci.DataAccess;
import alma.ACS.jbaci.PropertyInitializationFailed;
import alma.ACSErr.CompletionHolder;
import alma.acs.alarmsystem.source.AlarmQueue;
import alma.acs.component.ComponentLifecycleException;
import alma.acs.container.ContainerServices;
import alma.acs.exceptions.AcsJException;
import alma.maciErrType.wrappers.AcsJComponentCleanUpEx;
import cta.acs.opcua.da.UaDataSupport;
import cta.acs.opcua.da.UaMethodSupport;
import cta.acs.opcua.da.extension.UaDataSupportBoolean;
import cta.acs.opcua.da.extension.UaDataSupportDoubles;
import cta.acs.opcua.da.extension.UaDataSupportFloats;
//import cta.acs.opcua.da.extension.UaDataSupportIntegers;
import cta.acs.opcua.da.extension.UaDataSupportLongs;
import cta.acs.opcua.da.extension.UaDataSupportUInt16;
import cta.acs.opcua.da.extension.UaDataSupportUInt64;
import cta.ctsarraycontolsystemsst.CTSArrayContolSystemOperations;

import cta.ctsarraycontolsystemsst.CTSArrayContolSystemImpl.UaDataSupportIntegers;

/**
 * Implementation of the Camera Test Setup interface with OPC UA client
 * functionality.
 *
 * This component is a bride for other components to get data from a sPLC
 *
 * The URI and the node names are defined in the CDB.
 *
 * @author vsliusar, UA
 * @author yrenier, unige
 *
 * @version $Revision: 001 $ : $LastChangedDate: 2015-12-13 12:51:23 +0200 (Sun,
 *          13 Dec 2015) $
 */
public class CTSArrayContolSystemImpl extends CharacteristicComponentImpl implements CTSArrayContolSystemOperations {
	//protected Logger logger;

	public CTSArrayContolSystemImpl() {
		super();
		//this.logger = LogManager.getLogger(this.getClass());
	}

	protected static final String OPCUA_URI = "opc_uri";
	protected static final String OPCUA_VAR = "_var";

	private static final String METHODS_CTS="Methods_CTS";

	private static final String KEY_OPCUATIME="opcuaTime";
	private static final String KEY_BOARD0_AC_DCDC="board0_AC_DCDC";
	private static final String KEY_BOARD0_DC_DCDC="board0_DC_DCDC";
	private static final String KEY_BOARD0_DC_DAC="board0_DC_DAC";
	private static final String KEY_PATCH1_AC_DAC="patch1_AC_DAC";

	private Map<String, Property> mapProperties = new HashMap<String, Property>();
	private Map<String, DataAccess> mapDataAccess = new HashMap<String, DataAccess>();
	
	/**
	 * Creates connection to URI defined in the CDB and then connects to the
	 * nodes specified for the properties.
	 */
	
	@Override
	public void initialize(ContainerServices containerServices) throws ComponentLifecycleException {
		super.initialize(containerServices);		
		try {
            // Creates keys for properties
			createPropertyLong(KEY_OPCUATIME);
			createPropertyLong(KEY_BOARD0_AC_DCDC);
			createPropertyLong(KEY_BOARD0_DC_DCDC);
			createPropertyLong(KEY_BOARD0_DC_DAC);
			createPropertyLong(KEY_PATCH1_AC_DAC);
		} catch (Exception e) {
			m_logger.warning("Exception occured while initializing: " + e.toString());
			return;
		}
	}

    // Overide functions to get properties.
	@Override
	public ROlongLong opcuaTime() {
		return (ROlongLong) getProperty(KEY_OPCUATIME);
	}
	@Override
	public ROlongLong board0_AC_DCDC() {
		return (ROlongLong) getProperty(KEY_BOARD0_AC_DCDC);
	}
	@Override
	public ROlongLong board0_DC_DCDC() {
		return (ROlongLong) getProperty(KEY_BOARD0_DC_DCDC);
	}
	@Override
	public ROlongLong board0_DC_DAC() {
		return (ROlongLong) getProperty(KEY_BOARD0_DC_DAC);
	}
	@Override
	public ROlongLong patch1_AC_DAC() {
		return (ROlongLong) getProperty(KEY_PATCH1_AC_DAC);
	}

    // overide functions to call OPCUA functions
	@Override
	public int set_ac_level(int inPatch, int inLevel) {
		return execUAMethod(METHODS_CTS, "set_ac_level", new Object[] {inPatch, inLevel});
	}

	@Override
	public int set_dc_level(int inBoard, int inLevel) {
		return execUAMethod(METHODS_CTS, "set_dc_level", new Object[] {inBoard, inLevel});
	}
	
	@Override
	public int set_led_status(String inLedType, int inLed, boolean inStatus) {
		return execUAMethod(METHODS_CTS, "set_led_status", new Object[] {inLedType, inLed, inStatus});
	}

	@Override
	public int all_on(String inLedType, int inLevel) {
		return execUAMethod(METHODS_CTS, "all_on", new Object[] {inLedType, inLevel});
	}
	
	@Override
	public int set_DAC_all_DC(int inLevelDC) {
		return execUAMethod(METHODS_CTS, "set_DAC_all_DC", new Object[] {inLevelDC});
	}
	
	@Override
	public int set_DAC_all(int inLevelAC, int inLevelDC) {
		return execUAMethod(METHODS_CTS, "set_DAC_all", new Object[] {inLevelAC, inLevelDC});
	}
	
	@Override
	public int switch_all(boolean inStatus) {
		return execUAMethod(METHODS_CTS, "switch_all", new Object[] {inStatus});
	}
	
	public double delta_ms(long in) {
		return (((long)System.nanoTime() - in)/1e6); 
	}
	
	public Property getProperty(String key) throws IllegalArgumentException {
		Property property = mapProperties.get(key);
		if (property == null)
			throw new IllegalArgumentException("invalid name: " + key);
		return property;
	}

	public DataAccess getDataAccess(String key) throws IllegalArgumentException {
		DataAccess dataAccess = mapDataAccess.get(key);
		if (dataAccess == null) {
			throw new IllegalArgumentException("invalid name: " + key);
		}
		return dataAccess;
	}

	protected Property addProperty(Property property) {
		String name = property.name();
		if (name == null) {
			throw new IllegalArgumentException("key == null");
		}
		if (mapProperties.put(name, property) != null) {
			throw new IllegalArgumentException("duplicate property name: " + name);
		}
		return property;
	}
	
	protected Property addProperty(String name, Property property) {
		if (name == null) {
			throw new IllegalArgumentException("key == null");
		}
		if (mapProperties.put(name, property) != null) {
			throw new IllegalArgumentException("duplicate property name: " + name);
		}
		return property;
	}

	protected DataAccess addDataAccess(String name, DataAccess dataAccess) {
		if (name == null) {
			throw new IllegalArgumentException("key == null");
		}
		if (mapDataAccess.put(name, dataAccess) != null) {
			throw new IllegalArgumentException("duplicate property name: " + name);
		}
		return dataAccess;
	}

	
	//// Used ////////////////////
	protected ROlong createPropertyBoolean(String element, String name, String NodeID) throws PropertyInitializationFailed {
		//m_logger.warning("name: " + name + ", NodeID: " + NodeID);
		ROlongImpl impl = new ROlongImpl(element, this, addDataAccess(name, createDataAccessBoolean(name, NodeID)));
		return (ROlong) addProperty(name, ROlongHelper.narrow(registerProperty(impl, new ROlongPOATie(impl))));
	}

	protected ROlongLong createPropertyInt(String element, String name, String NodeID) throws PropertyInitializationFailed {
		//m_logger.warning("name: " + name + ", NodeID: " + NodeID);
		ROlongLongImpl impl = new ROlongLongImpl(element, this, addDataAccess(name, createDataAccess(name, NodeID)));
		return (ROlongLong) addProperty(name, ROlongLongHelper.narrow(registerProperty(impl, new ROlongLongPOATie(impl))));
	}

	protected ROdouble createPropertyDouble(String element, String name, String NodeID) throws PropertyInitializationFailed {
		ROdoubleImpl impl = new ROdoubleImpl(element, this, addDataAccess(name, createDataAccess(name, NodeID)));
		return (ROdouble) addProperty(name, ROdoubleHelper.narrow(registerProperty(impl, new ROdoublePOATie(impl))));
	}	

	
	protected ROlongSeq createPropertyIntegers(String element, String name, String NodeID) throws PropertyInitializationFailed {
		//m_logger.warning("integers name: " + name + ", NodeID: " + NodeID);
		ROlongSeqImpl impl = new ROlongSeqImpl(element, this, addDataAccess(name, createDataAccessIntegers(name, NodeID)));
		return (ROlongSeq) addProperty(name, ROlongSeqHelper.narrow(registerProperty(impl, new ROlongSeqPOATie(impl))));
	}
	protected ROlongSeq createPropertyBooleans(String element, String name, String NodeID) throws PropertyInitializationFailed {
		//m_logger.warning("integers name: " + name + ", NodeID: " + NodeID);
		ROlongSeqImpl impl = new ROlongSeqImpl(element, this, addDataAccess(name, createDataAccessBooleans(name, NodeID)));
		return (ROlongSeq) addProperty(name, ROlongSeqHelper.narrow(registerProperty(impl, new ROlongSeqPOATie(impl))));
	}
	protected ROdoubleSeq createPropertyDoubles(String element, String name, String NodeID) throws PropertyInitializationFailed {
		//m_logger.warning("doubles name: " + name + ", NodeID: " + NodeID);
		ROdoubleSeqImpl impl = new ROdoubleSeqImpl(element, this, addDataAccess(name, createDataAccessDoubles(name, NodeID)));
		return (ROdoubleSeq) addProperty(name, ROdoubleSeqHelper.narrow(registerProperty(impl, new ROdoubleSeqPOATie(impl))));
	}	

	public UaDataSupport createDataAccessIntegers(String name, String NodeID) throws PropertyInitializationFailed {
		//m_logger.warning("Creating data access support integers: " + name + " from " + NodeID);
		return new UaDataSupportIntegers(getPropertyCharacteristic(OPCUA_URI), new String[] {NodeID});
	}
	public UaDataSupport createDataAccessDoubles(String name, String NodeID) throws PropertyInitializationFailed {
		//m_logger.warning("Creating data access support doubles: " + name + " from " + NodeID);
		return new UaDataSupportDoubles(getPropertyCharacteristic(OPCUA_URI), new String[] {NodeID});
	}
	public UaDataSupport createDataAccessBooleans(String name, String NodeID) throws PropertyInitializationFailed {
		//m_logger.warning("Creating data access support integers: " + name + " from " + NodeID);
		return new UaDataSupportIntegers(getPropertyCharacteristic(OPCUA_URI), new String[] {NodeID});
	}

	//////////////////////////////
	
	
	protected ROstring createPropertyStr(String name) throws PropertyInitializationFailed {
		ROstringImpl impl = new ROstringImpl(name, this, addDataAccess(name, createDataAccess(name)));
		return (ROstring) addProperty(ROstringHelper.narrow(registerProperty(impl, new ROstringPOATie(impl))));
	}

	/*
	protected ROlong createPropertyInt(String name) throws PropertyInitializationFailed {
		ROlongImpl impl = new ROlongImpl(name, this, addDataAccess(name, createDataAccessInt16(name)));
		return (ROlong) addProperty(ROlongHelper.narrow(registerProperty(impl, new ROlongPOATie(impl))));
	}
	*/

	protected ROdouble createPropertyDouble(String name) throws PropertyInitializationFailed {
		ROdoubleImpl impl = new ROdoubleImpl(name, this, addDataAccess(name, createDataAccess(name)));
		return (ROdouble) addProperty(ROdoubleHelper.narrow(registerProperty(impl, new ROdoublePOATie(impl))));
	}

	protected ROlongLong createPropertyLong(String name) throws PropertyInitializationFailed {
		ROlongLongImpl impl = new ROlongLongImpl(name, this, addDataAccess(name, createDataAccess(name)));
		return (ROlongLong) addProperty(ROlongLongHelper.narrow(registerProperty(impl, new ROlongLongPOATie(impl))));
	}
	
	protected ROfloat createPropertyFloat(String name) throws PropertyInitializationFailed {
		ROfloatImpl impl = new ROfloatImpl(name, this, addDataAccess(name, createDataAccess(name)));
		return (ROfloat) addProperty(ROfloatHelper.narrow(registerProperty(impl, new ROfloatPOATie(impl))));
	}
	
	protected ROboolean createPropertyBoolean(String name) throws PropertyInitializationFailed {
		RObooleanImpl impl = new RObooleanImpl(name, this, addDataAccess(name, createDataAccess(name)));
		return (ROboolean) addProperty(RObooleanHelper.narrow(registerProperty(impl, new RObooleanPOATie(impl))));
	}

	protected ROlong createPropertyBoolean(String name, String NodeID) throws PropertyInitializationFailed {
		//m_logger.warning("name: " + name + ", NodeID: " + NodeID);
		ROlongImpl impl = new ROlongImpl(name, this, addDataAccess(name, createDataAccessBoolean(name, NodeID)));
		return (ROlong) addProperty(ROlongHelper.narrow(registerProperty(impl, new ROlongPOATie(impl))));
	}

	protected ROlongLong createPropertyInt(String name, String NodeID) throws PropertyInitializationFailed {
		//m_logger.warning("name: " + name + ", NodeID: " + NodeID);
		ROlongLongImpl impl = new ROlongLongImpl(name, this, addDataAccess(name, createDataAccess(name, NodeID)));
		return (ROlongLong) addProperty(ROlongLongHelper.narrow(registerProperty(impl, new ROlongLongPOATie(impl))));
	}

	protected ROdouble createPropertyDouble(String name, String NodeID) throws PropertyInitializationFailed {
		ROdoubleImpl impl = new ROdoubleImpl(name, this, addDataAccess(name, createDataAccess(name, NodeID)));
		return (ROdouble) addProperty(ROdoubleHelper.narrow(registerProperty(impl, new ROdoublePOATie(impl))));
	}

	protected ROfloat createPropertyFloat(String name, String NodeID) throws PropertyInitializationFailed {
		ROfloatImpl impl = new ROfloatImpl(name, this, addDataAccess(name, createDataAccess(name, NodeID)));
		return (ROfloat) addProperty(ROfloatHelper.narrow(registerProperty(impl, new ROfloatPOATie(impl))));
	}
	
	public UaDataSupport createDataAccess(String name, String NodeID) throws PropertyInitializationFailed {
		//m_logger.warning("Creating data access support: " + name + " from " + NodeID);
		return new UaDataSupport(getPropertyCharacteristic(OPCUA_URI), NodeID);
	}

	public UaDataSupportBoolean createDataAccessBoolean(String name, String NodeID) throws PropertyInitializationFailed {
		//m_logger.fine("Creating 'UInt16' data access support: " + name);
		return new UaDataSupportBoolean(getPropertyCharacteristic(OPCUA_URI), NodeID);
	}
	
	public UaDataSupport createDataAccess(String name) throws PropertyInitializationFailed {
		//m_logger.warning("Creating data access support: " + name);
		return new UaDataSupport(getPropertyCharacteristic(OPCUA_URI), getPropertyCharacteristic(name.concat(OPCUA_VAR)));
	}
	public UaDataSupportLongs createDataAccessShort(String name) throws PropertyInitializationFailed {
		//m_logger.fine("Creating 'Short' data access support: " + name);
		return new UaDataSupportLongs(getPropertyCharacteristic(OPCUA_URI), getPropertyCharacteristic(name.concat(OPCUA_VAR)));
	}
	public UaDataSupportUInt16 createDataAccessUInt16(String name) throws PropertyInitializationFailed {
		//m_logger.fine("Creating 'UInt16' data access support: " + name);
		return new UaDataSupportUInt16(getPropertyCharacteristic(OPCUA_URI), getPropertyCharacteristic(name.concat(OPCUA_VAR)));
	}
	public UaDataSupportUInt64 createDataAccessUInt64(String name) throws PropertyInitializationFailed {
		//m_logger.fine("Creating 'UInt64' data access support: " + name);
		return new UaDataSupportUInt64(getPropertyCharacteristic(OPCUA_URI), getPropertyCharacteristic(name.concat(OPCUA_VAR)));
	}
	public UaDataSupportBoolean createDataAccessBoolean(String name) throws PropertyInitializationFailed {
		//m_logger.fine("Creating 'UInt16' data access support: " + name);
		return new UaDataSupportBoolean(getPropertyCharacteristic(OPCUA_URI), getPropertyCharacteristic(name.concat(OPCUA_VAR)));
	}
	public UaDataSupportFloats createDataAccessFloat(String name) throws PropertyInitializationFailed {
		//m_logger.fine("Creating 'UInt16' data access support: " + name);
		return new UaDataSupportFloats(getPropertyCharacteristic(OPCUA_URI), getPropertyCharacteristic(name.concat(OPCUA_VAR)));
	}

	public int execUAMethod(String name, String method, Object[] Params) {
		try {
			m_logger.warning("Creating method support: " + method + " ("+ name + ", " + getPropertyCharacteristic(name.concat(OPCUA_VAR)) + ")");
			UaMethodSupport m = new UaMethodSupport(getPropertyCharacteristic(OPCUA_URI), getPropertyCharacteristic(name.concat(OPCUA_VAR)));
			Object[] out = m.call(getPropertyCharacteristic(name.concat(OPCUA_VAR)), method, Params);
			Long res = (Long)out[0];
			if (res != 0) m_logger.warning("Method " + method + " exited with an error code " + res.toString());
			return res.intValue();
		} catch (Exception e) {
			m_logger.warning("Error occured: " + e.getMessage());
			return 1;
		}
	}

	public String getPropertyCharacteristic(String name) throws PropertyInitializationFailed {
		try {
			if (characteristicModelImpl == null) {
				throw new NoSuchCharacteristic("characteristicModelImpl == null", name, m_instanceName);
			}
			return characteristicModelImpl.getString(name);
		} catch (Exception e) {
			throw new PropertyInitializationFailed("illegal parameter: " + e.getLocalizedMessage(), e);
		}
	}

	public static boolean isNumeric(String str)
	{
	  try
	  {
	    double d = Double.parseDouble(str);
	  }
	  catch(NumberFormatException nfe)
	  {
	    return false;
	  }
	  return true;
	}

	/**
	 * Closes Properties and calls super.cleanUp().	 */
	@Override
	public void cleanUp() throws AcsJComponentCleanUpEx {
		m_logger.fine("Cleaning up the component ...");
		for (DataAccess dataAccess : mapDataAccess.values()) {
			UaDataSupport uaDataAccess = (UaDataSupport) dataAccess;
			try {
				uaDataAccess.close();
			} catch (Throwable e) {
				m_logger.warning("[" + m_instanceName + "] Close " + Arrays.asList(uaDataAccess.getNodeIdRefs()) + " failed: " + e.getMessage());
			}
		}
		try {
			super.cleanUp();
		} catch (Throwable e) {
			m_logger.warning("[" + m_instanceName + "] Clenup component failed: " + e.getMessage());
		}
	}

	
}

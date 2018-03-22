/*
 * Copyright (c) 2014, CTA collaboration. All rights reserved.
 * 
 * @author birsine (HU), mdavid (DESY)
 */
package cta.ctsarraycontolsystemsst.CTSArrayContolSystemImpl;

import java.util.logging.Logger;

import org.omg.PortableServer.Servant;

import alma.ACS.ACSComponentOperations;
import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ComponentHelper;
import cta.ctsarraycontolsystemsst.CTSArrayContolSystemOperations;
import cta.ctsarraycontolsystemsst.CTSArrayContolSystemPOATie;

/**
 * Component helper class. Generated for convenience, but can be modified by the
 * component developer.
 * 
 * @author birsine, HU
 * @author David Melkumyan, DESY
 */
public class CTSArrayContolSystemComponentHelper extends ComponentHelper {
	/**
	 * Constructor
	 * 
	 * @param containerLogger
	 *            logger used only by the parent class.
	 */
	public CTSArrayContolSystemComponentHelper(Logger containerLogger) {
		super(containerLogger);
	}

	/**
	 * @see alma.acs.container.ComponentHelper#_createComponentImpl()
	 */
	@Override
	protected ComponentLifecycle _createComponentImpl() {
		return new CTSArrayContolSystemImpl();
	}

	/**
	 * @see alma.acs.container.ComponentHelper#_getPOATieClass()
	 */
	@Override
	protected Class<? extends Servant> _getPOATieClass() {
		return CTSArrayContolSystemPOATie.class;
	}

	/**
	 * @see alma.acs.container.ComponentHelper#getOperationsInterface()
	 */
	@Override
	protected Class<? extends ACSComponentOperations> _getOperationsInterface() {
		return CTSArrayContolSystemOperations.class;
	}

}

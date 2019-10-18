/*******************************************************************************
 *        __                  _     _ _   
 *     /\ \ \__ _ _ __   ___ | |__ (_) |_ 
 *    /  \/ / _` | '_ \ / _ \| '_ \| | __|
 *   / /\  / (_| | | | | (_) | |_) | | |_ 
 *   \_\ \/ \__,_|_| |_|\___/|_.__/|_|\__|
 * 
 * *****************************************************************************
 * 
 *  Author: Willi Menapace <willi.menapace@gmail.com>
 * 
 ******************************************************************************/

package it.webapp.spatial;

import java.math.BigDecimal;

/**
 * A rectangular region
 */
public class RectangularGeoFence implements GeoFence {

	private BigDecimal latLow;
	private BigDecimal latHigh;
	private BigDecimal lngLow;
	private BigDecimal lngHigh;

	public RectangularGeoFence(BigDecimal latLow, BigDecimal latHigh, BigDecimal lngLow, BigDecimal lngHigh) {
		if(latLow == null || latHigh == null ||
		   lngLow == null || lngHigh == null) {
			throw new IllegalArgumentException("Coordinates must not be null");
		}
		
		this.latLow = latLow;
		this.latHigh = latHigh;
		this.lngLow = lngLow;
		this.lngHigh = lngHigh;
	}

	public BigDecimal getLatLow() {
		return latLow;
	}

	public BigDecimal getLatHigh() {
		return latHigh;
	}

	public BigDecimal getLngLow() {
		return lngLow;
	}

	public BigDecimal getLngHigh() {
		return lngHigh;
	}
	
	@Override
	public RectangularGeoFence getBounds() {
		return this;
	}
}

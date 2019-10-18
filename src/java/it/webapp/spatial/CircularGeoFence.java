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
 * A circular geographical region
 */
public class CircularGeoFence implements GeoFence {

    
        private static final double FROM_KM_TO_M = 1.852; //Conversion from km to M
	private BigDecimal latCenter;
	private BigDecimal lngCenter;
	private double radius;

	public CircularGeoFence(BigDecimal latCenter, BigDecimal lngCenter, double radius) {
		if(latCenter == null || lngCenter == null) {
			throw new IllegalArgumentException("Coordinates must not be null");
		}
		if (radius < 0) {
			throw new IllegalArgumentException("Radius must be positive");
		}
		
		this.latCenter = latCenter;
		this.lngCenter = lngCenter;
		this.radius = radius;
                
	}

	public BigDecimal getLatCenter() {
		return latCenter;
	}

	public BigDecimal getLngCenter() {
		return lngCenter;
	}

	public double getRadius() {
		return radius;
	}	
	
        /**
         * Gets coordinates in sexagesimal system and converts them in decimal system
         * 
         * @param degrees degrees of coordinates
         * @param minutes minutes of coordinates
         * @param seconds seconds of coordinates
         * @return coordinates in decimal system
         */
        public BigDecimal getDecimalDegreesCoordinates(BigDecimal degrees, BigDecimal minutes, BigDecimal seconds) {
            BigDecimal degreeRemainder = BigDecimal.ZERO;
            BigDecimal minuteRemainder = BigDecimal.ZERO;       

            if(seconds.compareTo(BigDecimal.valueOf(60)) >= 0) {
                BigDecimal[] quotientAndRemainder = seconds.divideAndRemainder(BigDecimal.valueOf(60));
                minuteRemainder = quotientAndRemainder[0].stripTrailingZeros();
                seconds = quotientAndRemainder[1]; 
            }

            minutes = minutes.add(minuteRemainder);
            if(minutes.compareTo(BigDecimal.valueOf(60)) >= 0) {
                BigDecimal[] quotientAndRemainder = minutes.divideAndRemainder(BigDecimal.valueOf(60));
                degreeRemainder = quotientAndRemainder[0].stripTrailingZeros();
                minutes = quotientAndRemainder[1];   
            }

            degrees = degrees.add(degreeRemainder);

            BigDecimal decimalDegreesCoordinates = degrees.add(BigDecimal.valueOf(((minutes.doubleValue()*60+seconds.doubleValue())/3600)));

            return decimalDegreesCoordinates;
        }

        /**
         * Gets the upper limit latitude given a radius in nautical miles and a latitude center in sexagesimal system 
         * 
         * @param latDegrees degrees of latitude center
         * @param latMinutes minutes of latitude center
         * @param latSeconds seconds of latitude center
         * @param radiusM radius in nautical miles
         * @return upper latitude limit in decimal system
         */
        private BigDecimal getLatHigh(BigDecimal latDegrees, BigDecimal latMinutes, BigDecimal latSeconds, double radiusM) {
            BigDecimal latHigh;
            BigDecimal radiusBigDecimal = new BigDecimal(radiusM);

            BigDecimal[] quotientAndRemainder = radiusBigDecimal.divideAndRemainder(BigDecimal.ONE);
            BigDecimal entirePartRadiusBigDecimal = quotientAndRemainder[0].stripTrailingZeros();
            BigDecimal fractionalPartRadiusBigDecimal = quotientAndRemainder[1];

            BigDecimal latHighMinutes = latMinutes.add(entirePartRadiusBigDecimal);
            BigDecimal latHighSeconds = latSeconds.add(fractionalPartRadiusBigDecimal.multiply(BigDecimal.valueOf(60)));

            latHigh = getDecimalDegreesCoordinates(latDegrees, latHighMinutes, latHighSeconds);

            return latHigh;
        }

        /**
         * Gets the lower limit latitude given a radius in nautical miles and a latitude center in sexagesimal system
         * 
         * @param latDegrees degrees of latitude center
         * @param latMinutes minutes of latitude center
         * @param latSeconds seconds of latitude center
         * @param radiusM radius in nautical miles
         * @return lower latitude limit in decimal system
         */
        private BigDecimal getLatLow(BigDecimal latDegrees, BigDecimal latMinutes, BigDecimal latSeconds, double radiusM) {
            BigDecimal latLow;
            BigDecimal radiusBigDecimal = new BigDecimal(radiusM);

            BigDecimal[] quotientAndRemainder = radiusBigDecimal.divideAndRemainder(BigDecimal.ONE);
            BigDecimal entirePartRadiusBigDecimal = quotientAndRemainder[0].stripTrailingZeros();
            BigDecimal fractionalPartRadiusBigDecimal = quotientAndRemainder[1];

            BigDecimal latLowMinutes = latMinutes.subtract(entirePartRadiusBigDecimal);
            BigDecimal latLowSeconds = latSeconds.subtract(fractionalPartRadiusBigDecimal.multiply(BigDecimal.valueOf(60)));

            latLow = getDecimalDegreesCoordinates(latDegrees, latLowMinutes, latLowSeconds);

            return latLow;
        }

        /**
         * Gets the upper limit longitude given a degrees approximation and a longitude center in sexagesimal system
         * 
         * @param lngDegrees degrees of longitude center
         * @param lngMinutes minutes of longitude center
         * @param lngSeconds seconds of longitude center
         * @param degreesApproximation approximation to calculate the longitude limit
         * @return upper longitude limit in decimal system
         */
        private BigDecimal getLngHigh(BigDecimal lngDegrees, BigDecimal lngMinutes, BigDecimal lngSeconds, double degreesApproximation) {
            BigDecimal lngHigh;
            BigDecimal radiusBigDecimal = new BigDecimal(degreesApproximation);

            BigDecimal[] quotientAndRemainder = radiusBigDecimal.divideAndRemainder(BigDecimal.ONE);
            BigDecimal entirePartRadiusBigDecimal = quotientAndRemainder[0].stripTrailingZeros();
            BigDecimal fractionalPartRadiusBigDecimal = quotientAndRemainder[1];

            BigDecimal lngHighMinutes = lngMinutes.add(entirePartRadiusBigDecimal);
            BigDecimal lngHighSeconds = lngSeconds.add(fractionalPartRadiusBigDecimal.multiply(BigDecimal.valueOf(60)));

            lngHigh = getDecimalDegreesCoordinates(lngDegrees, lngHighMinutes, lngHighSeconds);

            return lngHigh;
        }

        /**
         * Gets the lower limit longitude given a degrees approximation and a longitude center in sexagesimal system
         * 
         * @param lngDegrees degrees of longitude center
         * @param lngMinutes minutes of longitude center
         * @param lngSeconds seconds of longitude center
         * @param degreesApproximation approximation to calculate the longitude limit
         * @return lower longitude limit in decimal system
         */
        private BigDecimal getLngLow(BigDecimal lngDegrees, BigDecimal lngMinutes, BigDecimal lngSeconds, double degreesApproximation) {
            BigDecimal lngLow;
            BigDecimal radiusBigDecimal = new BigDecimal(degreesApproximation);

            BigDecimal[] quotientAndRemainder = radiusBigDecimal.divideAndRemainder(BigDecimal.ONE);
            BigDecimal entirePartRadiusBigDecimal = quotientAndRemainder[0].stripTrailingZeros();
            BigDecimal fractionalPartRadiusBigDecimal = quotientAndRemainder[1];

            BigDecimal lngLowMinutes = lngMinutes.subtract(entirePartRadiusBigDecimal);
            BigDecimal lngLowSeconds = lngSeconds.subtract(fractionalPartRadiusBigDecimal.multiply(BigDecimal.valueOf(60)));
            
            lngLow = getDecimalDegreesCoordinates(lngDegrees, lngLowMinutes, lngLowSeconds);

            return lngLow;
        }
        
        /**
         * Gets the limits coordinates on which to perform the search
         * 
         * @return the four limits coordinates
         */
	@Override
	public RectangularGeoFence getBounds() {
                BigDecimal lat = getLatCenter();
                BigDecimal lng = getLngCenter();
                double radiusKm = getRadius();
                double radiusM = radiusKm/FROM_KM_TO_M;

                BigDecimal fractionalPartLatDegrees = lat.remainder(BigDecimal.ONE);
                BigDecimal latDegrees = lat.subtract(fractionalPartLatDegrees).stripTrailingZeros();
                BigDecimal fractionalPartLatMinutes = fractionalPartLatDegrees.multiply(BigDecimal.valueOf(60)).remainder(BigDecimal.ONE);
                BigDecimal latMinutes = fractionalPartLatDegrees.multiply(BigDecimal.valueOf(60)).subtract(fractionalPartLatMinutes).stripTrailingZeros();
                BigDecimal latSeconds = fractionalPartLatMinutes.multiply(BigDecimal.valueOf(60)).stripTrailingZeros();

                BigDecimal latHigh = getLatHigh(latDegrees, latMinutes, latSeconds, radiusM);
                BigDecimal latLow = getLatLow(latDegrees, latMinutes, latSeconds, radiusM);

                double factorApproximation = (0.54/Math.cos(Math.toRadians(latDegrees.doubleValue())));
                double degreesApproximation = radiusKm * factorApproximation;

                BigDecimal[] lngDegreeAndFractionalPartLngDegree = lng.divideAndRemainder(BigDecimal.ONE);
                BigDecimal lngDegrees = lngDegreeAndFractionalPartLngDegree[0].stripTrailingZeros();
                BigDecimal fractionalPartLngDegrees = lngDegreeAndFractionalPartLngDegree[1];
                BigDecimal[] lngMinuteAndFractionalPartLngMinute = fractionalPartLngDegrees.multiply(BigDecimal.valueOf(60)).divideAndRemainder(BigDecimal.ONE);
                BigDecimal lngMinutes = lngMinuteAndFractionalPartLngMinute[0].stripTrailingZeros();
                BigDecimal fractionalPartLngMinutes = lngMinuteAndFractionalPartLngMinute[1];
                BigDecimal lngSeconds = fractionalPartLngMinutes.multiply(BigDecimal.valueOf(60)).stripTrailingZeros();

                BigDecimal lngHigh = getLngHigh(lngDegrees, lngMinutes, lngSeconds, degreesApproximation);
                BigDecimal lngLow = getLngLow(lngDegrees, lngMinutes, lngSeconds, degreesApproximation);
                       
		RectangularGeoFence rectangularGeoFence = new RectangularGeoFence(latLow, latHigh, lngLow, lngHigh);
                return rectangularGeoFence;
	}
	

        
}

package ch.epfl.javions.aircraft;

import java.util.Objects;

/**
 * AircraftData
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 *
 * @param registration (AircraftRegistration) : Immatriculation of the plane
 * @param typeDesignator (AircraftTypeDesignator) : Indicates the type of the plane
 * @param model (String) : the model of the plane
 * @param description (AircraftDescription) : Description of the plane
 * @param wakeTurbulenceCategory (WakeTurbulenceCategory) : The wake turbulence category
 */
public record AircraftData(AircraftRegistration registration, AircraftTypeDesignator typeDesignator,
                           String model, AircraftDescription description,
                           WakeTurbulenceCategory wakeTurbulenceCategory) {

    /**
     * This constructor verifies if none of the given parameters are null when the object is constructed.
     */
    public AircraftData {
        Objects.requireNonNull(registration);
        Objects.requireNonNull(typeDesignator);
        Objects.requireNonNull(model);
        Objects.requireNonNull(description);
        Objects.requireNonNull(wakeTurbulenceCategory);
    }
}

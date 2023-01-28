package com.driver.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private SpotType spotType;
    private boolean occupied;
    private int pricePerHour;

    @OneToMany(mappedBy = "spot",cascade = CascadeType.ALL)
    private List<Reservation> reservationList;

    @ManyToOne
    @JoinColumn
    private ParkingLot parkingLot;


    public Spot(){

    }
    public Spot(int numberOfWheels,int pricePerHour){
        this.pricePerHour=pricePerHour;
        this.occupied=false;
        if(numberOfWheels==2) this.spotType=SpotType.TWO_WHEELER;
        else if(numberOfWheels==4) this.spotType=SpotType.FOUR_WHEELER;
        else this.spotType=SpotType.OTHERS;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public void setSpotType(SpotType spotType) {
        this.spotType = spotType;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(int pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
}

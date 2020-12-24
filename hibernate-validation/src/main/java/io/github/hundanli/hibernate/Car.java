package io.github.hundanli.hibernate;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/24 10:09
 */
public class Car {
    @NotNull
    private String manufacturer;
    @NotNull
    @Size(min = 2, max = 14)
    private String licensePlate;
    @Min(2)
    private int seatCount;

    public Car(@NotNull String manufacturer, @NotNull @Size(min = 2, max = 14) String licensePlate, @Min(2) int seatCount) {
        this.manufacturer = manufacturer;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }
}

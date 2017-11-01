package innlevering.com.javaeightwork;


import java.util.List;

/**
 * @author Leo-Andreas Ervik
 */
public class Lake {

    private final String name;
    private final List<String> country;
    private final String continent;
    private final Double area;
    private final Double length;
    private final Double maxDepth;

    public Lake(String name, List<String> country, String continent, Double area, Double length, Double maxDepth) {
        this.name = name;
        this.country = country;
        this.continent = continent;
        this.area = area;
        this.length = length;
        this.maxDepth = maxDepth;
    }

    public String getName() {
        return name;
    }

    public List<String> getCountry() {
        return country;
    }

    public String getContinent() {
        return continent;
    }

    public Double getArea() {
        return area;
    }

    public Double getLength() {
        return length;
    }

    public Double getMaxDepth() {
        return maxDepth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lake lake = (Lake) o;

        if (name != null ? !name.equals(lake.name) : lake.name != null) return false;
        if (country != null ? !country.equals(lake.country) : lake.country != null) return false;
        if (continent != null ? !continent.equals(lake.continent) : lake.continent != null) return false;
        if (area != null ? !area.equals(lake.area) : lake.area != null) return false;
        if (length != null ? !length.equals(lake.length) : lake.length != null) return false;
        return maxDepth != null ? maxDepth.equals(lake.maxDepth) : lake.maxDepth == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (continent != null ? continent.hashCode() : 0);
        result = 31 * result + (area != null ? area.hashCode() : 0);
        result = 31 * result + (length != null ? length.hashCode() : 0);
        result = 31 * result + (maxDepth != null ? maxDepth.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Lake{" +
                "name='" + name + '\'' +
                ", country=" + country +
                ", continent='" + continent + '\'' +
                ", area=" + area +
                ", length=" + length +
                ", maxDepth=" + maxDepth +
                '}';
    }
}
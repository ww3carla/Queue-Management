module org.example.pt2025_30229_bozintan_carla_assignment2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.pt2025_30229_bozintan_carla_assignment2 to javafx.fxml;
    exports org.example.pt2025_30229_bozintan_carla_assignment2;
}
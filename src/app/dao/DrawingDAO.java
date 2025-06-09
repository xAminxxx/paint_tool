package app.dao;

public class DrawingDAO {

    private static DrawingDAO instance;

    private DrawingDAO() {
    }

    public static DrawingDAO getInstance() {
        if (instance == null) {
            instance = new DrawingDAO();
        }
        return instance;
    }

    public void saveDrawing(String name, String data) {
        System.out.println("Sauvegarde du dessin '" + name + "' dans la base de données");
    }

    public String loadDrawing(String name) {
        System.out.println("Chargement du dessin '" + name + "' depuis la base de données");
        return "{\"shapes\":[{\"type\":\"Rectangle\",\"x1\":100.00,\"y1\":100.00,\"x2\":200.00,\"y2\":200.00,\"color\":\"0x000000ff\"}]}";
    }
}

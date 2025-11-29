package interfaces;

public interface Maintainable {
    public void scheduleMaintenance();
    public boolean needsMaintenance();
    public void performMaintenance();
}
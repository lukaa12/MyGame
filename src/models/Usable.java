package models;

public interface Usable {
//    Jeśli gracz się zbliży to wyświetl info o możliwości interakcji
    boolean isReachable();
//    Jeśli gracz chce użyć przedmiotu zostanie wywołana metoda use
    void use();
}

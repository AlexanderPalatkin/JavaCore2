package lesson01;

public class Track implements Obstaclable {
    int length;

    public Track(int length) {
        this.length = length;
    }

    @Override
    public boolean toRun(int maxLength) {
        return (maxLength >= length);
    }

    @Override
    public boolean toJump(int maxHeight) {
        return false;
    }

    public String toString() {
        return "дорожку";
    }

}

package lesson01;

public class Human implements JumpableRunnable {

    String name;
    int maxHeight;
    int maxLength;
    public String getInfo() { return "Cat " + name; }

    public Human(String name, int maxHeight, int maxLength) {
        this.name = name;
        this.maxHeight = maxHeight;
        this.maxLength = maxLength;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public void jump() {
        System.out.println("Human jump");
    }

    @Override
    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public void run() {
        System.out.println("Human ran");
    }

    @Override
    public String toString() {
        return name;
    }
}

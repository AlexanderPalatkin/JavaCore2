package lesson01;

public class Main {
    public static void main(String[] args){
        JumpableRunnable[] members = {
                new Human("Sasha", 2, 17),
                new Human("Misha", 1, 8),
                new Cat("Kisa", 2, 12),
                new Robot("Robocop", 0, 10)
        };

        Obstaclable[] obstacles = {
                new Wall(2),
                new Wall(3),
                new Track(20)
        };

        for (JumpableRunnable member : members) {
            System.out.println("Препятствия проходит " + member);
            boolean winner = true;
            for (Obstaclable obstacle : obstacles) {
                System.out.println(member + " пробует пройти " + obstacle);
                if (obstacle.toJump(member.getMaxHeight()) ||
                        obstacle.toRun(member.getMaxLength())) {
                    System.out.println("Получилось");
                } else {
                    winner = false;
                    System.out.println("Получилось");
                    break;
                }
            }

            if(winner) {
                System.out.println(member + " прошёл");
            } else {
                System.out.println(member + " проиграл");
            }
            System.out.println();
        }
    }
}

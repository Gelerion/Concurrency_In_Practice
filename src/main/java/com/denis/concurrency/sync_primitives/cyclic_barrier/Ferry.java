package com.denis.concurrency.sync_primitives.cyclic_barrier;

import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier реализует шаблон синхронизации Барьер. Циклический барьер является точкой синхронизации,
 * в которой указанное количество параллельных потоков встречается и блокируется. Как только все потоки
 * прибыли, выполняется опционное действие (или не выполняется, если барьер был инициализирован без него), и,
 * после того, как оно выполнено, барьер ломается и ожидающие потоки «освобождаются». В конструктор барьера
 * (CyclicBarrier(int parties) и CyclicBarrier(int parties, Runnable barrierAction)) обязательно передается
 * количество сторон, которые должны «встретиться», и, опционально, действие, которое должно произойти,
 * когда стороны встретились, но перед тем когда они будут «отпущены».
 * <p>
 * Барьер похож на CountDownLatch, но главное различие между ними в том, что вы не можете заново использовать
 * «замок» после того, как его счётчик достигнет нуля, а барьер вы можете использовать снова, даже после того,
 * как он сломается. CyclicBarrier является альтернативой метода join(), который «собирает» потоки только после
 * того, как они выполнились.
 */
public class Ferry {
    /**
     * Рассмотрим следующий пример. Существует паромная переправа. Паром может переправлять одновременно по
     * три автомобиля. Чтобы не гонять паром лишний раз, нужно отправлять его, когда у переправы соберется минимум три автомобиля.
     */
    private static final CyclicBarrier BARRIER = new CyclicBarrier(3, new FerryBoat());


    //Инициализируем барьер на три потока и таском, который будет выполняться, когда
    //у барьера соберется три потока. После этого, они будут освобождены.
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 9; i++) {
            new Thread(new Car(i)).start();
            Thread.sleep(400);
        }
    }

    //Таск, который будет выполняться при достижении сторонами барьера
    private static class FerryBoat implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                System.out.println("Паром переправил автомобили!");
            } catch (InterruptedException e) {
            }
        }
    }

    //Стороны, которые будут достигать барьера
    public static class Car implements Runnable {
        private int carNumber;

        public Car(int carNumber) {
            this.carNumber = carNumber;
        }

        @Override
        public void run() {
            try {
                System.out.printf("Автомобиль №%d подъехал к паромной переправе.\n", carNumber);
                //Для указания потоку о том что он достиг барьера, нужно вызвать метод await()
                //После этого данный поток блокируется, и ждет пока остальные стороны достигнут барьера
                BARRIER.await();
                System.out.printf("Автомобиль №%d продолжил движение.\n", carNumber);
            } catch (Exception e) {
            }

        }
    }
}

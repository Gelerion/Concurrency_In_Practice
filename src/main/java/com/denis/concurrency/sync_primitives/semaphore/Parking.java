package com.denis.concurrency.sync_primitives.semaphore;

import com.denis.concurrency.Threads;

import java.util.concurrent.Semaphore;

/**
 * Синхронизатор Semaphore реализует шаблон синхронизации Семафор. Чаще всего, семафоры необходимы,
 * когда нужно ограничить доступ к некоторому общему ресурсу. В конструктор этого класса
 * (Semaphore(int permits) или Semaphore(int permits, boolean fair)) обязательно передается количество потоков,
 * которому семафор будет разрешать одновременно использовать заданный ресурс.
 */
public class Parking {
/*    Рассмотрим следующий пример. Существует парковка, которая одновременно может вмещать не более 5 автомобилей.
    Если парковка заполнена полностью, то вновь прибывший автомобиль должен подождать пока не освободится хотя
    бы одно место. После этого он сможет припарковаться.*/

    //Парковочное место занято - true, свободно - false
    private static final boolean[] PARKING_PLACES = new boolean[5];

    //Устанавливаем флаг "справедливый", в таком случае метод
    //aсquire() будет раздавать разрешения в порядке очереди
    private static final Semaphore SEMAPHORE = new Semaphore(5, true);

    public static void main(String[] args) {
        for (int i = 0; i < 7; i++) {
            new Thread(new Car(i)).start();
            Threads.sleepQuietly(400);
        }
    }

    private static class Car implements Runnable {
        private int carNumber;

        public Car(int carNumber) {
            this.carNumber = carNumber;
        }

        @Override
        public void run() {
            System.out.printf("Автомобиль №%d подъехал к парковке.\n", carNumber);

            try {
                //acquire() запрашивает доступ к следующему за вызовом этого метода блоку кода,
                //если доступ не разрешен, поток вызвавший этот метод блокируется до тех пор,
                //пока семафор не разрешит досту
                SEMAPHORE.acquire();

                int parkingNumber = -1;

                //В примере Semaphore парковка не синхронизирована между заехавшими на неё автомобилями.
                //В результате несколько автомобилей могут одновременно занять одно место.

                //Ищем свободное место и паркуемся
                synchronized (PARKING_PLACES) {
                    for (int i = 0; i < 5; i++) {
                        if (!PARKING_PLACES[i]) {
                            PARKING_PLACES[i] = true;  //занимаем его
                            parkingNumber = i;         //Наличие свободного места, гарантирует семафор
                            System.out.printf("Автомобиль №%d припарковался на месте %d.\n", carNumber, i);
                            break;
                        }
                    }
                }

                Thread.sleep(5000);       //Уходим за покупками, к примеру

                synchronized (PARKING_PLACES) {
                    PARKING_PLACES[parkingNumber] = false; //Освобождаем место
                }

                //release(), напротив, освобождает ресурс
                SEMAPHORE.release();
                System.out.printf("Автомобиль №%d покинул парковку.\n", carNumber);
            } catch (InterruptedException e) { /*NOP*/ }
        }
    }

}

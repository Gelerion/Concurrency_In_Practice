package com.denis.concurrency.dead_lock;

/**
 *  If one thread calls Model.updateModel() while another thread simultaneously calls View.updateView(), the first thread could obtain the
 *  Model's lock and wait for the View's lock, while the other obtains the View's lock and waits forever for the Model's lock.
 */
public class AntoherDeadLock
{
	public class Model {
		private View myView;
		public synchronized void updateModel(Object someArg) {
			doSomething(someArg);
			myView.somethingChanged();
		}

		public synchronized Object getSomething() {
			return someMethod();
		}

		private void doSomething(Object someArg){}
		private Object someMethod(){return null;}
	}
	public class View {
		private Model myModel;

		public synchronized void somethingChanged() {
			doSomething();
		}
		public synchronized void updateView() {
			Object o = myModel.getSomething();
		}

		private void doSomething(){}
		private Object someMethod(){return null;}
	}
}

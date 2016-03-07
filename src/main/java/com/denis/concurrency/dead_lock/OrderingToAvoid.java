package com.denis.concurrency.dead_lock;

public class OrderingToAvoid
{
/*	public void transferMoney(Account fromAccount,
							  Account toAccount,
							  DollarAmount amountToTransfer) {
		Account firstLock, secondLock;
		if (fromAccount.accountNumber() == toAccount.accountNumber())
			throw new Exception("Cannot transfer from account to itself");
		else if (fromAccount.accountNumber() < toAccount.accountNumber()) {
			firstLock = fromAccount;
			secondLock = toAccount;
		}
		else {
			firstLock = toAccount;
			secondLock = fromAccount;
		}
		synchronized (firstLock) {
			synchronized (secondLock) {
				if (fromAccount.hasSufficientBalance(amountToTransfer) {
					fromAccount.debit(amountToTransfer);
					toAccount.credit(amountToTransfer);
				}
			}
		}
	}*/
}

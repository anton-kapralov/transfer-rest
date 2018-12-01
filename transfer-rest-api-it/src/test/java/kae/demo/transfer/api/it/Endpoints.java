package kae.demo.transfer.api.it;

/** */
final class Endpoints {

  static final String HEALTH_ENDPOINT = "health";
  static final String USERS_ENDPOINT = "users";

  static final String ACCOUNTS_PATH = "/${userId}/accounts";
  static final String ACCOUNT_PATH = ACCOUNTS_PATH + "/${accountId}";
  static final String TRANSACTIONS_PATH = ACCOUNTS_PATH + "/${accountId}/transactions";
  static final String TRANSACTION_PATH = TRANSACTIONS_PATH + "/${transactionId}";
  static final String BANK_ACCOUNT_TRANSACTIONS_PATH = "/1/accounts/1/transactions";

  private Endpoints() {}
}

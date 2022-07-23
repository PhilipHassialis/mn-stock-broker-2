package com.hassialis.philip.broker.wallet;

import com.hassialis.philip.broker.api.RestApiResponse;
import com.hassialis.philip.broker.data.InMemoryAccountStore;
import com.hassialis.philip.broker.wallet.error.CustomError;
import com.hassialis.philip.broker.wallet.error.FiatCurrencyNotSupportedException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

import static com.hassialis.philip.broker.data.InMemoryAccountStore.ACCOUNT_ID;

@Controller("/account/wallets")
public record WalletController(InMemoryAccountStore store) {
  public static final List<String> SUPPORTED_FIAT_CURRENCIES = List.of("EUR", "USD", "GBP", "CHF");
  private static final Logger LOG = LoggerFactory.getLogger(WalletController.class);

  @Get(produces = MediaType.APPLICATION_JSON)
  public Collection<Wallet> get() {
    return store.getWallets(ACCOUNT_ID);
  }

  @Post(value = "/deposit", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public HttpResponse<RestApiResponse> depositFiatMoney(@Body DepositFiatMoney deposit) {
    if (!SUPPORTED_FIAT_CURRENCIES.contains(deposit.symbol().value()))
      return HttpResponse.badRequest().body(new CustomError(HttpStatus.BAD_REQUEST.getCode(),
          "UNSUPPORTED_FIAT_CURRENCY", String.format("Supported currencies %s", SUPPORTED_FIAT_CURRENCIES)));
    var wallet = store.depositToWallet(deposit);
    LOG.debug("Wallet {} after deposit {}", wallet, deposit.amount());
    return HttpResponse.ok(wallet);
  }

  @Post(value = "/withdraw", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public Wallet withdrawFiatMoney(@Body WithdrawFiatMoney withdraw) {
    if (!SUPPORTED_FIAT_CURRENCIES.contains(withdraw.symbol().value())) {
      throw new FiatCurrencyNotSupportedException(String.format("Supported currencies %s", SUPPORTED_FIAT_CURRENCIES));
    }
    var wallet = store.withdrawFromWallet(withdraw);
    LOG.debug("Wallet {} after withdraw {}", wallet, withdraw.amount());
    return wallet;
  }

}

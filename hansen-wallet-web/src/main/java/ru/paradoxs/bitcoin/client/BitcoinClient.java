package ru.paradoxs.bitcoin.client;


import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import ru.paradoxs.bitcoin.client.exceptions.BitcoinClientException;
import ru.paradoxs.bitcoin.http.HttpSession;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BitcoinClient {

    private HttpSession session;

    private static BigDecimal getBigDecimal(JSONObject jsonObject, String key) throws JSONException {
        String string = jsonObject.getString(key);
        BigDecimal bigDecimal = new BigDecimal(string);
        return bigDecimal;
    }

    public BitcoinClient(String host, String login, String password, int port) {
        this.session = null;

        try {
            UsernamePasswordCredentials e = new UsernamePasswordCredentials(login, password);
            URI uri = new URI("http", (String) null, host, port, (String) null, (String) null, (String) null);
            this.session = new HttpSession(uri, e);
        } catch (URISyntaxException var7) {
            throw new BitcoinClientException("This host probably doesn\'t have correct syntax: " + host, var7);
        }
    }

    public BitcoinClient(String host, String login, String password) {
        this(host, login, password, 8332);
    }

    public List<String> getAddressesByAccount(String account) {
        if (account == null) {
            account = "";
        }

        try {
            JSONArray e = (new JSONArray()).element(account);
            JSONObject request = this.createRequest("getaddressesbyaccount", e);
            JSONObject response = this.session.sendAndReceive(request);
            JSONArray result = (JSONArray) response.get("result");
            int size = result.size();
            ArrayList list = new ArrayList();

            for (int i = 0; i < size; ++i) {
                list.add(result.getString(i));
            }

            return list;
        } catch (JSONException var9) {
            throw new BitcoinClientException("Got incorrect JSON for this account: " + account, var9);
        }
    }

    public BigDecimal getBalance() {
        try {
            JSONObject e = this.createRequest("getbalance");
            JSONObject response = this.session.sendAndReceive(e);
            return getBigDecimal(response, "result");
        } catch (JSONException var3) {
            throw new BitcoinClientException("Exception when getting balance", var3);
        }
    }

    public BigDecimal getBalance(String account) {
        if (account == null) {
            account = "";
        }

        try {
            JSONArray e = (new JSONArray()).element(account);
            JSONObject request = this.createRequest("getbalance", e);
            JSONObject response = this.session.sendAndReceive(request);
            return getBigDecimal(response, "result");
        } catch (JSONException var5) {
            throw new BitcoinClientException("Exception when getting balance", var5);
        }
    }

    public int getBlockCount() {
        try {
            JSONObject e = this.createRequest("getblockcount");
            JSONObject response = this.session.sendAndReceive(e);
            return response.getInt("result");
        } catch (JSONException var3) {
            throw new BitcoinClientException("Exception when getting block count", var3);
        }
    }

    public int getBlockNumber() {
        try {
            JSONObject e = this.createRequest("getblocknumber");
            JSONObject response = this.session.sendAndReceive(e);
            return response.getInt("result");
        } catch (JSONException var3) {
            throw new BitcoinClientException("Exception when getting the block number", var3);
        }
    }

    public int getConnectionCount() {
        try {
            JSONObject e = this.createRequest("getconnectioncount");
            JSONObject response = this.session.sendAndReceive(e);
            return response.getInt("result");
        } catch (JSONException var3) {
            throw new BitcoinClientException("Exception when getting the number of connections", var3);
        }
    }

    public long getHashesPerSecond() {
        try {
            JSONObject e = this.createRequest("gethashespersec");
            JSONObject response = this.session.sendAndReceive(e);
            return response.getLong("result");
        } catch (JSONException var3) {
            throw new BitcoinClientException("Exception when getting the number of calculated hashes per second", var3);
        }
    }

    public BigDecimal getDifficulty() {
        try {
            JSONObject e = this.createRequest("getdifficulty");
            JSONObject response = this.session.sendAndReceive(e);
            return getBigDecimal(response, "result");
        } catch (JSONException var3) {
            throw new BitcoinClientException("Exception when getting the difficulty", var3);
        }
    }

    public boolean getGenerate() {
        try {
            JSONObject e = this.createRequest("getgenerate");
            JSONObject response = this.session.sendAndReceive(e);
            return response.getBoolean("result");
        } catch (JSONException var3) {
            throw new BitcoinClientException("Exception when getting whether the server is generating coins or not", var3);
        }
    }

    public void setGenerate(boolean isGenerate, int processorsCount) {
        try {
            JSONArray e = (new JSONArray()).element(isGenerate).element(processorsCount);
            JSONObject request = this.createRequest("setgenerate", e);
            this.session.sendAndReceive(request);
        } catch (JSONException var5) {
            throw new BitcoinClientException("Exception when setting whether the server is generating coins or not", var5);
        }
    }

    public ServerInfo getServerInfo() {
        try {
            JSONObject e = this.createRequest("getinfo");
            JSONObject response = this.session.sendAndReceive(e);
            JSONObject result = (JSONObject) response.get("result");
            ServerInfo info = new ServerInfo();
            info.setBalance(getBigDecimal(result, "balance"));
            info.setBlocks(result.getLong("blocks"));
            info.setConnections(result.getInt("connections"));
            info.setDifficulty(getBigDecimal(result, "difficulty"));
            info.setHashesPerSecond(result.getLong("hashespersec"));
            info.setIsGenerateCoins(result.getBoolean("generate"));
            info.setUsedCPUs(result.getInt("genproclimit"));
            info.setVersion(result.getString("version"));
            return info;
        } catch (JSONException var5) {
            throw new BitcoinClientException("Exception when getting the server info", var5);
        }
    }

    public String getAccount(String address) {
        try {
            JSONArray e = (new JSONArray()).element(address);
            JSONObject request = this.createRequest("getaccount", e);
            JSONObject response = this.session.sendAndReceive(request);
            return response.getString("result");
        } catch (JSONException var5) {
            throw new BitcoinClientException("Exception when getting the account associated with this address: " + address, var5);
        }
    }

    public void setAccountForAddress(String address, String account) {
        try {
            JSONArray e = (new JSONArray()).element(address).element(account);
            JSONObject request = this.createRequest("setaccount", e);
            this.session.sendAndReceive(request);
        } catch (JSONException var5) {
            throw new BitcoinClientException("Exception when setting the account associated with a given address", var5);
        }
    }

    public String getAccountAddress(String account) {
        if (account == null) {
            account = "";
        }

        try {
            JSONArray e = (new JSONArray()).element(account);
            JSONObject request = this.createRequest("getaccountaddress", e);
            JSONObject response = this.session.sendAndReceive(request);
            return response.getString("result");
        } catch (JSONException var5) {
            throw new BitcoinClientException("Exception when getting the new bitcoin address for receiving payments", var5);
        }
    }

    public BigDecimal getReceivedByAddress(String address, long minimumConfirmations) {
        try {
            JSONArray e = (new JSONArray()).element(address).element(minimumConfirmations);
            JSONObject request = this.createRequest("getreceivedbyaddress", e);
            JSONObject response = this.session.sendAndReceive(request);
            return getBigDecimal(response, "result");
        } catch (JSONException var7) {
            throw new BitcoinClientException("Exception when getting the total amount received by bitcoinaddress", var7);
        }
    }

    public BigDecimal getReceivedByAccount(String account, long minimumConfirmations) {
        try {
            JSONArray e = (new JSONArray()).element(account).element(minimumConfirmations);
            JSONObject request = this.createRequest("getreceivedbyaccount", e);
            JSONObject response = this.session.sendAndReceive(request);
            return getBigDecimal(response, "result");
        } catch (JSONException var7) {
            throw new BitcoinClientException("Exception when getting the total amount received for account: " + account, var7);
        }
    }

    public String help(String command) {
        try {
            JSONArray e = (new JSONArray()).element(command);
            JSONObject request = this.createRequest("help", e);
            JSONObject response = this.session.sendAndReceive(request);
            return response.getString("result");
        } catch (JSONException var5) {
            throw new BitcoinClientException("Exception when getting help for a command", var5);
        }
    }

    public List<AddressInfo> listReceivedByAddress(long minimumConfirmations, boolean includeEmpty) {
        try {
            JSONArray e = (new JSONArray()).element(minimumConfirmations).element(includeEmpty);
            JSONObject request = this.createRequest("listreceivedbyaddress", e);
            JSONObject response = this.session.sendAndReceive(request);
            JSONArray result = response.getJSONArray("result");
            int size = result.size();
            ArrayList list = new ArrayList();

            for (int i = 0; i < size; ++i) {
                AddressInfo info = new AddressInfo();
                JSONObject jObject = result.getJSONObject(i);
                info.setAddress(jObject.getString("address"));
                info.setAccount(jObject.getString("account"));
                info.setAmount(getBigDecimal(jObject, "amount"));
                info.setConfirmations(jObject.getLong("confirmations"));
                list.add(info);
            }

            return list;
        } catch (JSONException var13) {
            throw new BitcoinClientException("Exception when getting info about all received transactions by address", var13);
        }
    }

    public List<AccountInfo> listReceivedByAccount(long minimumConfirmations, boolean includeEmpty) {
        try {
            JSONArray e = (new JSONArray()).element(minimumConfirmations).element(includeEmpty);
            JSONObject request = this.createRequest("listreceivedbyaccount", e);
            JSONObject response = this.session.sendAndReceive(request);
            JSONArray result = response.getJSONArray("result");
            int size = result.size();
            ArrayList list = new ArrayList(size);

            for (int i = 0; i < size; ++i) {
                AccountInfo info = new AccountInfo();
                JSONObject jObject = result.getJSONObject(i);
                info.setAccount(jObject.getString("account"));
                info.setAmount(getBigDecimal(jObject, "amount"));
                info.setConfirmations(jObject.getLong("confirmations"));
                list.add(info);
            }

            return list;
        } catch (JSONException var13) {
            throw new BitcoinClientException("Exception when getting the received amount by account", var13);
        }
    }

    public List<TransactionInfo> listTransactions(String account, int count) {
        if (account == null) {
            account = "";
        }

        if (count <= 0) {
            throw new BitcoinClientException("count must be > 0");
        } else {
            try {
                JSONArray e = (new JSONArray()).element(account).element(count);
                JSONObject request = this.createRequest("listtransactions", e);
                JSONObject response = this.session.sendAndReceive(request);
                JSONArray result = response.getJSONArray("result");
                int size = result.size();
                ArrayList list = new ArrayList(size);

                for (int i = 0; i < size; ++i) {
                    JSONObject jObject = result.getJSONObject(i);
                    TransactionInfo info = this.parseTransactionInfoFromJson(jObject);
                    list.add(info);
                }

                return list;
            } catch (JSONException var12) {
                throw new BitcoinClientException("Exception when getting transactions for account: " + account, var12);
            }
        }
    }

    public TransactionInfo getTransaction(String txId) {
        try {
            JSONArray e = (new JSONArray()).element(txId);
            JSONObject request = this.createRequest("gettransaction", e);
            JSONObject response = this.session.sendAndReceive(request);
            JSONObject result = (JSONObject) response.get("result");
            return this.parseTransactionInfoFromJson(result);
        } catch (JSONException var6) {
            throw new BitcoinClientException("Exception when getting transaction info for this id: " + txId, var6);
        }
    }

    private TransactionInfo parseTransactionInfoFromJson(JSONObject jObject) throws JSONException {
        TransactionInfo info = new TransactionInfo();
        info.setAmount(getBigDecimal(jObject, "amount"));
        if (jObject.has("category")) {
            info.setCategory(jObject.getString("category"));
        }

        if (jObject.has("fee")) {
            info.setFee(getBigDecimal(jObject, "fee"));
        }

        if (jObject.has("message")) {
            info.setMessage(jObject.getString("message"));
        }

        if (jObject.has("to")) {
            info.setTo(jObject.getString("to"));
        }

        if (jObject.has("confirmations")) {
            info.setConfirmations(jObject.getLong("confirmations"));
        }

        if (jObject.has("txid")) {
            info.setTxId(jObject.getString("txid"));
        }
        if (jObject.has("otheraccount")) {
            info.setOtherAccount(jObject.getString("otheraccount"));
        }

        if (jObject.has("time")) {
            info.setTime(jObject.getLong("time"));
        }

        if (jObject.has("account")) {
            info.setAccount(jObject.getString("account"));
        }
        if (jObject.has("address")) {
            info.setAddress(jObject.getString("address"));
        }
        if (jObject.has("comment")) {
            info.setComment(jObject.getString("comment"));
        }
        if (jObject.has("blockhash")) {
            info.setBlockhash(jObject.getString("blockhash"));
        }
        if (jObject.has("blockindex")) {
            info.setBlockindex(jObject.getInt("blockindex"));
        }
        if (jObject.has("timereceived")) {
            info.setTimereceived(jObject.getLong("timereceived"));
        }
        if (jObject.has("blocktime")) {
            info.setBlocktime(jObject.getLong("blocktime"));
        }
        return info;
    }

    public List<TransactionInfo> listTransactions(String account) {
        return this.listTransactions(account, 10);
    }

    public List<TransactionInfo> listTransactions() {
        return this.listTransactions("", 10);
    }

    public WorkInfo getWork() {
        try {
            JSONObject e = this.createRequest("getwork");
            JSONObject response = this.session.sendAndReceive(e);
            JSONObject result = (JSONObject) response.get("result");
            WorkInfo info = new WorkInfo();
            info.setMidstate(result.getString("midstate"));
            info.setData(result.getString("data"));
            info.setHash1(result.getString("hash1"));
            info.setTarget(result.getString("target"));
            return info;
        } catch (JSONException var5) {
            throw new BitcoinClientException("Exception when getting work info", var5);
        }
    }

    public boolean getWork(String block) {
        try {
            JSONArray e = (new JSONArray()).element(block);
            JSONObject request = this.createRequest("getwork", e);
            JSONObject response = this.session.sendAndReceive(request);
            return response.getBoolean("result");
        } catch (JSONException var5) {
            throw new BitcoinClientException("Exception when trying to solve a block with getwork", var5);
        }
    }

    public String sendToAddress(String bitcoinAddress, BigDecimal amount, String comment, String commentTo) {
        amount = this.checkAndRound(amount);

        try {
            JSONArray e = (new JSONArray()).element(bitcoinAddress).element(amount).element(comment).element(commentTo);
            JSONObject request = this.createRequest("sendtoaddress", e);
            JSONObject response = this.session.sendAndReceive(request);
            return response.getString("result");
        } catch (JSONException var8) {
            throw new BitcoinClientException("Exception when sending bitcoins", var8);
        }
    }

    public String sendFrom(String account, String bitcoinAddress, BigDecimal amount, int minimumConfirmations, String comment, String commentTo) {
        if (account == null) {
            account = "";
        }

        if (minimumConfirmations <= 0) {
            throw new BitcoinClientException("minimumConfirmations must be > 0");
        } else {
            amount = this.checkAndRound(amount);

            try {
                JSONArray e = (new JSONArray()).element(account).element(bitcoinAddress).element(amount).element(minimumConfirmations).element(comment).element(commentTo);
                JSONObject request = this.createRequest("sendfrom", e);
                JSONObject response = this.session.sendAndReceive(request);
                return response.getString("result");
            } catch (JSONException var10) {
                throw new BitcoinClientException("Exception when sending bitcoins with sendFrom()", var10);
            }
        }
    }

    public boolean move(String fromAccount, String toAccount, BigDecimal amount, int minimumConfirmations, String comment) {
        if (fromAccount == null) {
            fromAccount = "";
        }

        if (toAccount == null) {
            toAccount = "";
        }

        if (minimumConfirmations <= 0) {
            throw new BitcoinClientException("minimumConfirmations must be > 0");
        } else {
            amount = this.checkAndRound(amount);

            try {
                JSONArray e = (new JSONArray()).element(fromAccount).element(toAccount).element(amount).element(minimumConfirmations).element(comment);
                JSONObject request = this.createRequest("move", e);
                JSONObject response = this.session.sendAndReceive(request);
                return response.getBoolean("result");
            } catch (JSONException var9) {
                throw new BitcoinClientException("Exception when moving " + amount + " bitcoins from account: \'" + fromAccount + "\' to account: \'" + toAccount + "\'", var9);
            }
        }
    }

    private BigDecimal checkAndRound(BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("0.01")) < 0) {
            throw new BitcoinClientException("The current machinery doesn\'t support transactions of less than 0.01 Bitcoins");
        } else if (amount.compareTo(new BigDecimal("21000000")) > 0) {
            throw new BitcoinClientException("Sorry dude, can\'t transfer that many Bitcoins");
        } else {
            amount = roundToTwoDecimals(amount);
            return amount;
        }
    }

    public void stop() {
        try {
            JSONObject e = this.createRequest("stop");
            this.session.sendAndReceive(e);
        } catch (JSONException var2) {
            throw new BitcoinClientException("Exception when stopping the bitcoin server", var2);
        }
    }

    public ValidatedAddressInfo validateAddress(String address) {
        try {
            JSONArray e = (new JSONArray()).element(address);
            JSONObject request = this.createRequest("validateaddress", e);
            JSONObject response = this.session.sendAndReceive(request);
            JSONObject result = (JSONObject) response.get("result");
            ValidatedAddressInfo info = new ValidatedAddressInfo();
            info.setIsValid(result.getBoolean("isvalid"));
            if (info.getIsValid()) {
                info.setIsMine(result.getBoolean("ismine"));
                info.setAddress(result.getString("address"));
            }

            return info;
        } catch (JSONException var7) {
            throw new BitcoinClientException("Exception when validating an address", var7);
        }
    }

    public void backupWallet(String destination) {
        try {
            JSONArray e = (new JSONArray()).element(destination);
            JSONObject request = this.createRequest("backupwallet", e);
            this.session.sendAndReceive(request);
        } catch (JSONException var4) {
            throw new BitcoinClientException("Exception when backing up the wallet", var4);
        }
    }

    protected static BigDecimal roundToTwoDecimals(BigDecimal amount) {
        BigDecimal amountTimes100 = amount.multiply(new BigDecimal(100)).add(new BigDecimal("0.5"));
        BigDecimal roundedAmountTimes100 = new BigDecimal(amountTimes100.intValue());
        BigDecimal roundedAmount = roundedAmountTimes100.divide(new BigDecimal(100.0D));
        return roundedAmount;
    }

    private JSONObject createRequest(String functionName, JSONArray parameters) throws JSONException {
        JSONObject request = new JSONObject();
        request.put("jsonrpc", "2.0");
        request.put("id", UUID.randomUUID().toString());
        request.put("method", functionName);
        request.put("params", parameters);
        return request;
    }

    private JSONObject createRequest(String functionName) throws JSONException {
        return this.createRequest(functionName, new JSONArray());
    }

}

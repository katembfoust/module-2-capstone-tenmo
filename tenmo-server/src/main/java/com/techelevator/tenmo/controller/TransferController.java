package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated")
public class TransferController {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TransferDao transferDao;

    private TransferTypeDao transferTypeDao;

    private TransferStatusDao transferStatusDao;

    public TransferController(){}


    public TransferController(AccountDao accountDao, UserDao userDao, TransferDao transferDao, TransferTypeDao transferTypeDao, TransferStatusDao transferStatusDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
        this.transferTypeDao = transferTypeDao;
        this.transferStatusDao = transferStatusDao;
    }

//    @PreAuthorize("permitAll")
//    @ResponseStatus(HttpStatus.CREATED)
//    @RequestMapping(path = "transfer/{transferTypeId}/{transferStatusId}/{accountTo}/{accountFrom}/{amount}", method = RequestMethod.POST)
//    public void createTransfer(@RequestBody Transfer transfer, @PathVariable Long transferTypeId, @PathVariable Long transferStatusId, @PathVariable Long accountTo, @PathVariable Long accountFrom, @PathVariable Double amount) {
//     transferDao.createTransfer(transfer, transferTypeId, transferStatusId,  accountTo, accountFrom, amount);
//    }

    @PreAuthorize("permitAll")
    @PostMapping(value = "transfer/create")
    public void createTrans(@RequestBody Transfer transfer) {
        Double amount = transfer.getAmount();
        Account accountFrom = accountDao.getAccountByAccountId(transfer.getAccountFrom());
        Account accountTo = accountDao.getAccountByAccountId(transfer.getAccountTo());



        accountDao.withdrawAccount(accountFrom, transfer.getAccountFrom(), amount);
        accountDao.depositAccount(accountTo, transfer.getAccountTo(), amount);

        transferDao.createTrans(transfer);
    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "transfers/account/{id}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByAccountId(@Valid @PathVariable Long id) {
        return null;
    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "transfers/user/{id}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByUserId(@PathVariable Long id) {
        return transferDao.getTransfersByUserId(id);
    }

    @PreAuthorize("permitAll")
    @GetMapping(path = "transferdetails/{id}")
    public TransferDetails[] listTransferDetails(@PathVariable Long id){
        return transferDao.getTransferDetails(id);
    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "transfer/update/{id}", method = RequestMethod.GET)
    public void updateTransfer(@RequestBody Transfer transfer, @PathVariable Long transferId) {
        transferDao.updateTransfer(transfer,transferId);
    }


}

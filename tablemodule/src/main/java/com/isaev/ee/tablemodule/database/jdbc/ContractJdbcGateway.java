package com.isaev.ee.tablemodule.database.jdbc;

import com.isaev.ee.tablemodule.database.utils.ConnectionUtils;
import com.isaev.ee.tablemodule.tables.Contract;
import org.apache.log4j.Logger;
import org.javamoney.moneta.Money;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class ContractJdbcGateway {

    private static final String MESSAGE_TEMPLATE_CANNOT_FIND_CONTRACT = "Cannot find contract by ID: %s";
    private static final String INSERTION_QUERY_TEMPLATE = "INSERT INTO contracts (contract_id, product_id, date_signed, revenue) VALUES (?, ?, ?, ?)";
    private static final int INSERTION_CONTRACT_ID_PARAMETER = 1;
    private static final int INSERTION_PRODUCT_ID_PARAMETER = 2;
    private static final int INSERTION_DATE_SIGNED_PARAMETER = 3;
    private static final int INSERTION_REVENUE_PARAMETER = 4;
    private static final String SELECTION_BY_ID_QUERY_TEMPLATE =
            "SELECT contracts.contract_id, " +
                    "contracts.product_id, " +
                    "contracts.date_signed, " +
                    "contracts.revenue " +
                    "FROM contracts " +
                    "WHERE contracts.contract_id = ?";
    private static final int SELECTION_BY_ID_CONTRACT_ID_PARAMETER = 1;
    private static final String PRODUCT_ID_NAME = "product_id";
    private static final String DATE_SIGNED_FIELD_NAME = "date_signed";
    private static final String REVENUE_FIELD_NAME = "revenue";
    private static final String USD_CURRENCY_CODE = "USD";

    public static final String MESSAGE_TEMPLATE_CANNOT_SAVE_THE_CONTRACT = "Cannot save the contract with ID: %s";

    private final static Logger logger = Logger.getLogger(ContractJdbcGateway.class);


    /**
     * Saves contract details into the database.
     *
     * @param contract
     */
    public void save(Contract contract) {
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERTION_QUERY_TEMPLATE)) {
            statement.setInt(INSERTION_CONTRACT_ID_PARAMETER, contract.getId());
            statement.setInt(INSERTION_PRODUCT_ID_PARAMETER, contract.getProductId());
            statement.setDate(INSERTION_DATE_SIGNED_PARAMETER, Date.valueOf(contract.getDateSigned()));
            statement.setBigDecimal(INSERTION_REVENUE_PARAMETER, contract.getRevenue().getNumberStripped());
            statement.addBatch();
            statement.executeBatch();
        } catch (SQLException e) {
            logger.error(String.format(MESSAGE_TEMPLATE_CANNOT_SAVE_THE_CONTRACT, contract.getId()), e);
        }
    }

    public Optional<Contract> findByContracId(int contractId) {

        Optional<Contract> contract = Optional.empty();

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECTION_BY_ID_QUERY_TEMPLATE)) {
            statement.setInt(SELECTION_BY_ID_CONTRACT_ID_PARAMETER, contractId);

            var resultSet = statement.executeQuery();
            resultSet.next();

            Money totalRevenue = Money.of(resultSet.getBigDecimal(REVENUE_FIELD_NAME), USD_CURRENCY_CODE);
            LocalDate recognitionDate = resultSet.getDate(DATE_SIGNED_FIELD_NAME).toLocalDate();
            int productId = resultSet.getInt(PRODUCT_ID_NAME);

            contract = Optional.of(new Contract(contractId, productId, recognitionDate, totalRevenue));

        } catch (SQLException e) {
            logger.error(String.format(MESSAGE_TEMPLATE_CANNOT_FIND_CONTRACT, contractId), e);
        }

        return contract;
    }



}

/*
 * JStock - Free Stock Market Software
 * Copyright (C) 2011 Yan Cheng CHEOK <yccheok@yahoo.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.yccheok.jstock.portfolio;

import org.yccheok.jstock.engine.*;

/**
 *
 * @author Owner
 */
public class Contract {

    public static class ContractBuilder implements Builder<Contract> {
        private final Stock stock;
        private final SimpleDate date;
        
        // Optional parameters - initialized to default values
        private Type type = Type.Buy;
        private double quantity = 0;
        private double price = 0.0;
        private double referencePrice = 0.0;
        private SimpleDate referenceDate = new SimpleDate();

        public ContractBuilder(Stock stock, SimpleDate date) {
            this.stock = stock;
            this.date = date;
        }
        
        public ContractBuilder type(Type val) {
            this.type = val;
            return this;
        }

        public ContractBuilder quantity(double val) {
            this.quantity = val;
            return this;
        }
        
        public ContractBuilder price(double val) {
            this.price = val;
            return this;
        }
        
        public ContractBuilder referencePrice(double val) {
            this.referencePrice = val;
            return this;
        }

        public ContractBuilder referenceDate(SimpleDate date) {
            this.referenceDate = date;
            return this;
        }

        @Override
        public Contract build() {
            return new Contract(this);
        }
        
    }
    
    public Stock getStock() {
        return stock;
    }

    public SimpleDate getDate() {
        return date;
    }

    public Type getType() {
        return type;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
    
    public double getReferencePrice() {
        return referencePrice;
    }
    
    public double getTotal() {
        return total;
    }
    
    public double getReferenceTotal() {
        return referenceTotal;
    }

    public SimpleDate getReferenceDate() {
        return this.referenceDate;
    }
    
    public enum Type
    {
        Buy,
        Sell
    }
    
    private Contract(ContractBuilder builder)
    {
        this.stock = builder.stock;
        this.date = builder.date;
        this.type = builder.type;
        this.quantity = builder.quantity;
        this.price = builder.price;
        this.referencePrice = builder.referencePrice;
        
        this.total = price * quantity;
        this.referenceTotal = referencePrice * quantity;
        this.referenceDate = builder.referenceDate;
    }
    
    /**
     * Derives a contract with new quantity from this contract.
     *
     * @param quantity new quantity
     * @return a contract with new quantity derived from this contract
     */
    public Contract deriveWithQuantity(double quantity) {
        ContractBuilder builder = new ContractBuilder(stock, date);
        return builder.type(type).quantity(quantity).price(price).referencePrice(referencePrice).build();
    }

    /**
     * Derives a contract with new price from this contract.
     * 
     * @param price new price
     * @return a contract with new price derived from this contract
     */
    public Contract deriveWithPrice(double price) {
        ContractBuilder builder = new ContractBuilder(stock, date);
        return builder.type(type).quantity(quantity).price(price).referencePrice(referencePrice).build();
    }

    public Contract(Contract contract) {
        stock = new Stock(contract.stock);
        date = new SimpleDate(contract.date);
        type = contract.type;
        quantity = contract.quantity;
        price = contract.price;
        referencePrice = contract.referencePrice;
        total = contract.total;
        referenceTotal = contract.referenceTotal;
        referenceDate = contract.referenceDate;
    }

    private Object readResolve() {
        /* For backward compatible. */
        if(referenceDate == null) {
            /* May yield incorrect logic. */
            /* Make buy date same as sell date. */
            referenceDate = date;
        }

        return this;
    }

    private final Stock stock;
    private final SimpleDate date;
    private final Type type;
    private final double quantity;
    private final double price;
    private final double referencePrice;
    private final double total;
    // Reference price for the contract. Only for selling type contract usage.
    // It means cost for owning a stock.
    private final double referenceTotal;
    // private final SimpleDate referenceDate;
    // Unalbe to make this as final, as we need backward compatible with xstream's readResolve
    private SimpleDate referenceDate;
}

/**
 * Copyright © 2014 - 2018 Leipzig University (Database Research Group)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradoop.flink.datagen.transactions.foodbroker;

import it.giacomobergami.init.ExecutionEnvironment;
import it.giacomobergami.io.NewFileWriter;
import it.giacomobergami.iterator.StreamableIterator;
import org.gradoop.common.model.impl.pojo.GraphTransaction;
import org.gradoop.common.model.impl.pojo.Vertex;
import org.gradoop.flink.datagen.transactions.foodbroker.config.FoodBrokerBroadcastNames;
import org.gradoop.flink.datagen.transactions.foodbroker.config.FoodBrokerConfig;
import org.gradoop.flink.datagen.transactions.foodbroker.functions.process.Brokerage;
import org.gradoop.flink.datagen.transactions.foodbroker.functions.process.ComplaintHandling;
import org.gradoop.flink.datagen.transactions.foodbroker.generators.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Generates a GraphCollection containing a foodbrokerage and a complaint handling process.
 */
public class FoodBroker {

  /**
   * Foodbroker configuration.
   */
  private final FoodBrokerConfig foodBrokerConfig;

  /**
   * Set which contains all customer vertices.
   */
  private StreamableIterator<Vertex> customers;
  /**
   * Set which contains all vendor vertices.
   */
  private StreamableIterator<Vertex> vendors;
  /**
   * Set which contains all logistic vertices.
   */
  private StreamableIterator<Vertex> logistics;
  /**
   * Set which contains all employee vertices.
   */
  private StreamableIterator<Vertex> employees;
  /**
   * Set which contains all product vertices.
   */
  private StreamableIterator<Vertex> products;

  /**
   * Valued constructor.
   *Brokerage
   * @param foodBrokerConfig Foodbroker configuration
   */
  public FoodBroker(FoodBrokerConfig foodBrokerConfig) {
    this.foodBrokerConfig = foodBrokerConfig;
  }

  public StreamableIterator<GraphTransaction> execute() {
    // Phase 1: Create MasterData
    initMasterData();

    ExecutionEnvironment env = ExecutionEnvironment.instance();

    // Phase 2.1: Run Brokerage
    List<Long> caseSeeds = env.generateSequence(1, foodBrokerConfig.getCaseCount());

    return ExecutionEnvironment.instance()
            .withBroadcastSet(customers, FoodBrokerBroadcastNames.BC_CUSTOMERS)
            .withBroadcastSet(vendors, FoodBrokerBroadcastNames.BC_VENDORS)
            .withBroadcastSet(logistics, FoodBrokerBroadcastNames.BC_LOGISTICS)
            .withBroadcastSet(employees, FoodBrokerBroadcastNames.BC_EMPLOYEES)
            .withBroadcastSet(products, FoodBrokerBroadcastNames.BC_PRODUCTS)
            .streamAsIterator(caseSeeds)
            .map(new Brokerage(foodBrokerConfig))
            .map(new ComplaintHandling(foodBrokerConfig));
  }


  /**
   * Initialises all maps which store reduced vertex information.
   */
  private void initMasterData() {
    customers = new CustomerGenerator(foodBrokerConfig).generate();
    vendors = new VendorGenerator(foodBrokerConfig).generate();
    logistics = new LogisticsGenerator(foodBrokerConfig).generate();
    employees = new EmployeeGenerator(foodBrokerConfig).generate();
    products = new ProductGenerator(foodBrokerConfig).generate();
  }

  public static void main(String args[]) throws IOException {
    StreamableIterator<GraphTransaction> it = new FoodBroker(FoodBrokerConfig.fromFile("config.json")).execute();
    File f = new File("data/");
    if (!f.exists()) {
      f.mkdirs();
    } else {
      if (!f.isFile()) {
        NewFileWriter nfw = new NewFileWriter("data/");
        while (it.hasNext()) {
          GraphTransaction x = it.next();
          nfw.writeGraph(x);
        }
      }
    }
  }

}

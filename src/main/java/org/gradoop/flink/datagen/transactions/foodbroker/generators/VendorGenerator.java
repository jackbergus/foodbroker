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
package org.gradoop.flink.datagen.transactions.foodbroker.generators;

import it.giacomobergami.iterator.StreamableIterator;
import org.gradoop.common.model.impl.pojo.Vertex;
import org.gradoop.flink.datagen.transactions.foodbroker.config.FoodBrokerBroadcastNames;
import org.gradoop.flink.datagen.transactions.foodbroker.config.FoodBrokerConfig;
import org.gradoop.flink.datagen.transactions.foodbroker.config.FoodBrokerVertexLabels;
import org.gradoop.flink.datagen.transactions.foodbroker.functions.masterdata.Vendor;
import org.gradoop.flink.datagen.transactions.foodbroker.tuples.MasterDataSeed;

import java.util.List;

/**
 * Generator for vertices which represent vendors.
 */
public class VendorGenerator extends BusinessRelationGenerator {

  /**
   * Valued constructor.
   *
   * @param foodBrokerConfig FoodBroker configuration.
   */
  public VendorGenerator(FoodBrokerConfig foodBrokerConfig) {
    super(foodBrokerConfig);
  }

  @Override
  public StreamableIterator<Vertex> generate() {
    List<MasterDataSeed> seeds = getMasterDataSeeds(FoodBrokerVertexLabels.VENDOR_VERTEX_LABEL);
    loadData();
    return env
            .withBroadcastSet(env.fromCollection(getAdjectives()), FoodBrokerBroadcastNames.ADJECTIVES_BC)
            .withBroadcastSet(env.fromCollection(getNouns()), FoodBrokerBroadcastNames.NOUNS_BC)
            .withBroadcastSet(env.fromCollection(getCities()), FoodBrokerBroadcastNames.CITIES_BC)
            .withBroadcastSet(env.fromCollection(getCompanies()), FoodBrokerBroadcastNames.COMPANIES_BC)
            .withBroadcastSet(env.fromCollection(getHoldings()), FoodBrokerBroadcastNames.HOLDINGS_BC)
            .streamAsIterator(seeds)
            .map(new Vendor(vertexFactory, foodBrokerConfig));
  }
}

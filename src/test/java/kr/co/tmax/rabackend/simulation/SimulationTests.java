package kr.co.tmax.rabackend.simulation;

import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.infrastructure.asset.AssetReaderImpl;
import kr.co.tmax.rabackend.infrastructure.simulation.SimulationReaderImpl;
import kr.co.tmax.rabackend.infrastructure.simulation.SimulationStoreImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class SimulationTests {

    @Autowired
    SimulationReaderImpl simulationReader;

    @Autowired
    SimulationStoreImpl simulationStore;

    @Autowired
    AssetReaderImpl assetReader;

    @Test
    void contextLoads() {
    }

    @Test
    void 시뮬레이션아이디로조회() {
        String id = "a6b38f21-9a2d-43a8-ac76-5b9c2e5b238b";
        Optional<Simulation> byId = simulationReader.findById(id);
        System.out.println("byId = " + byId);
    }

    @Test
    void 자산아이디로조회() {
        List<Asset> all = assetReader.findAll();
        System.out.println("all = " + all);
    }

    @Test
    void 전략들조회() {
        String id = "a6b38f21-9a2d-43a8-ac76-5b9c2e5b238b";
        Optional<Simulation> byId = simulationReader.findById(id);
        List<Strategy> s = byId.map(Simulation::getStrategies).orElse(null);
        System.out.println("s = " + s);
        System.out.println("s.size() = " + s.size());
    }

    @Test
    void 특정전략완료업데이트() {
        String id = "a6b38f21-9a2d-43a8-ac76-5b9c2e5b238b";
        String name = "PPO";
        Optional<Simulation> byId = simulationReader.findById(id);
        List<Strategy> s = byId.map(Simulation::getStrategies).orElse(null);
        String simulationId = byId.map(Simulation::getSimulationId).orElse(null);
        String userId = byId.map(Simulation::getUserId).orElse(null);
        List<String> assets = byId.map(Simulation::getAssets).orElse(null);
        LocalDate startDate = byId.map(Simulation::getStartDate).orElse(null);
        LocalDate endDate = byId.map(Simulation::getEndDate).orElse(null);
        Simulation build = null;
        int reb = byId.map(Simulation::getRebalancingPeriod).orElse(null);
        for (int i = 0; i < s.size(); i++) {
            if (s.get(i).getName().equals(name)) {
                s.get(i).setDone(true);
                build = Simulation.doneStrategy().simulationId(simulationId).userId(userId).rebalancingPeriod(reb).strategies(s).startDate(startDate).endDate(endDate)
                        .assets(assets).build();
                System.out.println("build = " + build);
                simulationStore.store(build); // update
            }
        }
        System.out.println("s = " + s);
    }

    @Test
    void 전략모두완료시_시뮬레이션업데이트() {
        String id = "28c6523f-a015-4802-be34-a6d738972f2f";
        String name = "PPO";
        String name1 = "EW";
        int cnt = 0;
        Optional<Simulation> byId = simulationReader.findById(id);
        List<Strategy> s = byId.map(Simulation::getStrategies).orElse(null);
        String simulationId = byId.map(Simulation::getSimulationId).orElse(null);
        String userId = byId.map(Simulation::getUserId).orElse(null);
        List<String> assets = byId.map(Simulation::getAssets).orElse(null);
        LocalDate startDate = byId.map(Simulation::getStartDate).orElse(null);
        LocalDate endDate = byId.map(Simulation::getEndDate).orElse(null);
        Simulation build = null;
        int reb = byId.map(Simulation::getRebalancingPeriod).orElse(null);
        for (int i = 0; i < s.size(); i++) {
            if (s.get(i).getName().equals(name)) {
                s.get(i).setDone(true);
                cnt++;
                build = Simulation.doneStrategy().simulationId(simulationId).userId(userId).rebalancingPeriod(reb).strategies(s).startDate(startDate).endDate(endDate)
                        .assets(assets).build();
                System.out.println("build = " + build);

                simulationStore.store(build); // update
            }
        }

        for (int i = 0; i < s.size(); i++) {
            if (s.get(i).getName().equals(name1)) {
                s.get(i).setDone(true);
                cnt++;
                build = Simulation.doneStrategy().simulationId(simulationId).userId(userId).rebalancingPeriod(reb).strategies(s).startDate(startDate).endDate(endDate)
                        .assets(assets).build();
                System.out.println("build = " + build);
                if (cnt == s.size()) {
                    Simulation build1 = Simulation.doneSimulation().isDone(true).simulationId(simulationId).userId(userId).rebalancingPeriod(reb).strategies(s)
                            .startDate(startDate).endDate(endDate).assets(assets).build();
                    System.out.println("build1 = " + build1);
                    simulationStore.store(build1);
                }
//                simulationStore.store(build); // update
            }
        }
        System.out.println("s = " + s);
    }
}

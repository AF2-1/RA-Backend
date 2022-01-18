package kr.co.tmax.rabackend.simulation;

import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.infrastructure.asset.AssetReaderImpl;
import kr.co.tmax.rabackend.infrastructure.simulation.SimulationReadImpl;
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
    SimulationReadImpl simulationReader;

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
    void 전략완료시_시뮬레이션업데이트() throws Exception {
        String id = "301a5396-20bb-4e9d-9182-764f683e8750";
        String name = "ppo";
        String name1 = "ew";
        Simulation simulation = simulationReader.findById(id).orElseThrow(() -> new Exception());
        simulation.update(id, name1);
        simulationStore.store(simulation); // update
    }
}

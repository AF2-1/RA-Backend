package kr.co.tmax.rabackend.external;

import kr.co.tmax.rabackend.domain.simulation.Simulation;

import java.util.List;

public interface KserveApiClient {
    void requestAA(Simulation simulation, List<String> strategyNames);
}

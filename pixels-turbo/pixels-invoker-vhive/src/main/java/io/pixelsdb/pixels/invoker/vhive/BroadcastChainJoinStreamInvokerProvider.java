/*
 * Copyright 2025 PixelsDB.
 *
 * This file is part of Pixels.
 *
 * Pixels is free software: you can redistribute it and/or modify
 * it under the terms of the Affero GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Pixels is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Affero GNU General Public License for more details.
 *
 * You should have received a copy of the Affero GNU General Public
 * License along with Pixels.  If not, see
 * <https://www.gnu.org/licenses/>.
 */
package io.pixelsdb.pixels.invoker.vhive;

import io.pixelsdb.pixels.common.turbo.FunctionService;
import io.pixelsdb.pixels.common.turbo.Invoker;
import io.pixelsdb.pixels.common.turbo.InvokerProvider;
import io.pixelsdb.pixels.common.turbo.WorkerType;
import io.pixelsdb.pixels.common.utils.ConfigFactory;

public class BroadcastChainJoinStreamInvokerProvider implements InvokerProvider
{
    private static final ConfigFactory config = ConfigFactory.Instance();

    @Override
    public Invoker createInvoker()
    {
        String broadcastChainJoinWorker = config.getProperty("broadcast.chain.join.worker.name");
        return new BroadcastChainJoinStreamInvoker(broadcastChainJoinWorker);
    }

    @Override
    public WorkerType workerType() { return WorkerType.BROADCAST_CHAIN_JOIN_STREAMING; }

    @Override
    public boolean compatibleWith(FunctionService functionService)
    {
        return functionService.equals(FunctionService.vhive);
    }
}
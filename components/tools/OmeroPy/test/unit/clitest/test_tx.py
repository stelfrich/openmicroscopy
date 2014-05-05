#!/usr/bin/env python
# -*- coding: utf-8 -*-

#
# Copyright (C) 2013 Glencoe Software, Inc. All Rights Reserved.
# Use is subject to license terms supplied in LICENSE.txt
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

"""
Test of the omero/plugins/tx.py module
"""

import pytest

from omero.api import IQueryPrx
from omero.api import IUpdatePrx
from omero.api import ServiceFactoryPrx
from omero.cli import CLI
from omero.clients import BaseClient
from omero.model import ProjectI
from omero.plugins.tx import NewObjectTxAction
from omero.plugins.tx import TxCmd
from omero.plugins.tx import TxControl
from omero.plugins.tx import TxState
from omero_ext.mox import IgnoreArg
from omero_ext.mox import Mox


class MockCLI(CLI):

    def conn(self, *args, **kwargs):
        return self._client

    def close(self, *args, **kwargs):
        pass

    def out(self, out):
        if hasattr(self, "_out"):
            self._out.append(out)
        else:
            self._out = [out]


@pytest.mark.clitx
class TxBase(object):

    def setup_method(self, method):
        self.mox = Mox()
        self.client = self.mox.CreateMock(BaseClient)
        self.sf = self.mox.CreateMock(ServiceFactoryPrx)
        self.query = self.mox.CreateMock(IQueryPrx)
        self.update = self.mox.CreateMock(IUpdatePrx)
        self.client.sf = self.sf
        self.cli = MockCLI()
        self.cli._client = self.client
        self.cli.set("tx.state", TxState(self.cli))

    def teardown_method(self, method):
        self.mox.UnsetStubs()
        self.mox.VerifyAll()

    def queries(self, obj):
        self.sf.getQueryService().AndReturn(self.query)
        self.query.get(IgnoreArg(), IgnoreArg(), IgnoreArg()).AndReturn(obj)

    def saves(self, obj):
        self.sf.getUpdateService().AndReturn(self.update)
        self.update.saveAndReturnObject(IgnoreArg()).AndReturn(obj)


class TestNewObjectTxAction(TxBase):

    def test_unknown_class(self):
        self.saves(ProjectI(1, False))
        self.mox.ReplayAll()
        state = TxState(self.cli)
        cmd = TxCmd(state, arg_list=["new", "Project", "name=foo"])
        action = NewObjectTxAction(state, cmd)
        action.go(self.cli, None)


class TestTxControl(TxBase):

    def setup_method(self, method):
        super(TestTxControl, self).setup_method(method)
        self.cli.register("tx", TxControl, "TEST")

    def test_simple_new_usage(self):
        self.saves(ProjectI(1, False))
        self.mox.ReplayAll()
        self.cli.invoke("tx new Project name=foo", strict=True)
        assert self.cli._out == ["Project:1"]

    def test_simple_update_usage(self):
        self.queries(ProjectI(1, True))
        self.saves(ProjectI(1, False))
        self.mox.ReplayAll()
        self.cli.invoke(("tx update Project:1 name=bar "
                        "description=loooong"), strict=True)
        assert self.cli._out == ["Project:1"]

QUnit.module( 'clipBoard' );

QUnit.test( 'clipBoard cut_paste_getChangedComponentsList_getRemovedComponentsList', function( assert ) {
	var clipBoard = ACS.clipBoard();
	var model = ACS.model("test.acs");
	var comp1 = ACS.component("comp1","asterics.FS20Receiver","desc",true,1,2,"actuator",false,true);
	var port1 = ACS.port('outP', comp1, 1, 0, 0, false);
	comp1.outputPortList.push(port1); 
	model.addComponent(comp1);
	var comp2 = ACS.component("comp2","asterics.Proximity","desc",true,1,2,"actuator",false,true);
	var port2 = ACS.port('inP', comp2, 0, 0, 0, false);
	comp2.inputPortList.push(port2);
	model.addComponent(comp2);
	var comp3 = ACS.component("comp3","someIdNotInCollection","desc",true,1,2,"actuator",false,false);
	model.addComponent(comp3);
	var otherComp = ACS.component("otherComp","irrelevantTypeID","desc",true,1,2,"actuator",false,false);
	var otherPort = ACS.port('otherPort', otherComp, 0, 0, 0, false);
	var dc1 = ACS.dataChannel('dataChannel1');
	dc1.setOutputPort(otherPort);
	dc1.setInputPort(otherPort);
	model.addDataChannel(dc1);
	var ec1 = ACS.eventChannel('eventChannel1');
	ec1.startComponent = otherComp;
	ec1.endComponent = otherComp;
	model.addEventChannel(ec1);
	var dc2 = ACS.dataChannel('dataChannel2');
	dc2.setOutputPort(port1);
	dc2.setInputPort(port2);
	model.addDataChannel(dc2);
	var ec2 = ACS.eventChannel('eventChannel2');
	ec2.startComponent = comp1;
	ec2.endComponent = comp2;
	model.addEventChannel(ec2);
	model.addItemToSelection(comp1);
	model.addItemToSelection(comp2);
	model.addItemToSelection(comp3);
	model.addItemToSelection(dc1);
	model.addItemToSelection(ec1);
	model.addItemToSelection(dc2);
	model.addItemToSelection(ec2);
	clipBoard.cut(model);
	assert.strictEqual(model.componentList.length, 0);
	assert.strictEqual(model.dataChannelList.length, 0);
	assert.strictEqual(model.eventChannelList.length, 0);
	var newModel = ACS.model('newTestModel.acs');
	clipBoard.paste(newModel);
	assert.strictEqual(newModel.componentList.length, 2);
	assert.strictEqual(newModel.componentList[0].getId(), 'comp1_c');
	assert.strictEqual(newModel.componentList[1].getId(), 'comp2_c');
	assert.strictEqual(newModel.dataChannelList.length, 1);
	assert.strictEqual(newModel.dataChannelList[0].getId(), 'dataChannel2_c');
	assert.strictEqual(newModel.eventChannelList.length, 1);
	assert.strictEqual(newModel.eventChannelList[0].getId(), 'eventChannel2_c');
	assert.strictEqual(clipBoard.getRemovedComponentsList()[0].getId(), 'comp3_c');
	assert.strictEqual(clipBoard.getChangedComponentsList().length, 2);
	assert.strictEqual(clipBoard.getChangedComponentsList()[1].getId(), 'comp2_c');	
});

QUnit.test( 'clipBoard copy_paste_getChangedComponentsList_getRemovedComponentsList', function( assert ) {
	var clipBoard = ACS.clipBoard();
	var model = ACS.model("test.acs");
	var comp1 = ACS.component("comp1","asterics.FS20Receiver","desc",true,1,2,"actuator",false,true);
	var port1 = ACS.port('outP', comp1, 1, 0, 0, false);
	comp1.outputPortList.push(port1); 
	model.addComponent(comp1);
	var comp2 = ACS.component("comp2","asterics.Proximity","desc",true,1,2,"actuator",false,true);
	var port2 = ACS.port('inP', comp2, 0, 0, 0, false);
	comp2.inputPortList.push(port2);
	model.addComponent(comp2);
	var comp3 = ACS.component("comp3","someIdNotInCollection","desc",true,1,2,"actuator",false,false);
	model.addComponent(comp3);
	var otherComp = ACS.component("otherComp","irrelevantTypeID","desc",true,1,2,"actuator",false,false);
	var otherPort = ACS.port('otherPort', otherComp, 0, 0, 0, false);
	var dc1 = ACS.dataChannel('dataChannel1');
	dc1.setOutputPort(otherPort);
	dc1.setInputPort(otherPort);
	model.addDataChannel(dc1);
	var ec1 = ACS.eventChannel('eventChannel1');
	ec1.startComponent = otherComp;
	ec1.endComponent = otherComp;
	model.addEventChannel(ec1);
	var dc2 = ACS.dataChannel('dataChannel2');
	dc2.setOutputPort(port1);
	dc2.setInputPort(port2);
	model.addDataChannel(dc2);
	var ec2 = ACS.eventChannel('eventChannel2');
	ec2.startComponent = comp1;
	ec2.endComponent = comp2;
	model.addEventChannel(ec2);
	model.addItemToSelection(comp1);
	model.addItemToSelection(comp2);
	model.addItemToSelection(comp3);
	model.addItemToSelection(dc1);
	model.addItemToSelection(ec1);
	model.addItemToSelection(dc2);
	model.addItemToSelection(ec2);
	clipBoard.copy(model);
	assert.strictEqual(model.componentList.length, 3);
	assert.strictEqual(model.dataChannelList.length, 2);
	assert.strictEqual(model.eventChannelList.length, 2);
	var newModel = ACS.model('newTestModel.acs');
	clipBoard.paste(newModel);
	assert.strictEqual(newModel.componentList.length, 2);
	assert.strictEqual(newModel.componentList[0].getId(), 'comp1_c');
	assert.strictEqual(newModel.componentList[1].getId(), 'comp2_c');
	assert.strictEqual(newModel.dataChannelList.length, 1);
	assert.strictEqual(newModel.dataChannelList[0].getId(), 'dataChannel2_c');
	assert.strictEqual(newModel.eventChannelList.length, 1);
	assert.strictEqual(newModel.eventChannelList[0].getId(), 'eventChannel2_c');
	assert.strictEqual(clipBoard.getRemovedComponentsList()[0].getId(), 'comp3_c');
	assert.strictEqual(clipBoard.getChangedComponentsList().length, 2);
	assert.strictEqual(clipBoard.getChangedComponentsList()[1].getId(), 'comp2_c');
});	
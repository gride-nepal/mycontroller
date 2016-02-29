/*
 * Copyright (C) 2015-2016 Jeeva Kandasamy (jkandasa@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
myControllerModule.controller('RawMessageController', function(alertService, $scope, displayRestError, TypesFactory, StatusFactory, $filter) {
  
  $scope.nodes = TypesFactory.getNodes();
  $scope.messageTypes = TypesFactory.getMessageTypes();
  
  $scope.rawMessage = {};
  $scope.rawMessage.isTxMessage = true;
  
  //Update sensors
  $scope.refreshSensors = function(nodeId){
      return TypesFactory.getSensors({id: nodeId});
  };
  
  $scope.refreshMessageSubTypes = function(messageTypeId){
      return TypesFactory.getMessageSubTypes({id:messageTypeId});
  };
  
  $scope.sendRawMessage = function () {
    StatusFactory.sendRawMessage($scope.rawMessage,function(response) {
      alertService.success($filter('translate')('RAW.NOTIFY_MESSAGE', $scope));
    },function(error){
      displayRestError.display(error);            
    });
  }
});

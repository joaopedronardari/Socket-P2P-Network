EP 1 - DE REDES

Grupo:
Adriano dos Santos Rodrigues da Silva - 8623910 
Guilherme Hernandes - 8598631
Jo�o Pedro Nardari - 8623865

=====================================================
1 - Classes
2 - Como compilar
3 - Como executar
4 - Observa��es
=====================================================

1 - Classes

1.1 - entity.User
	Nesta classe est�o agrupadas todos os atributos de cada usu�rio, como seu nome, senha, IP, porta em que espera conex�es e sua lista de amigos. 

1.2 - gui.WindowLogin
	Esta classe representa a tela de login, contendo os campos Usu�rio (nome), senha e ip do servidor. Todos os dados s�o informados pelo usu�rio 
	e s� realiza o logon quando o usu�rio dgitar um usu�rio e senha v�lido e um ip de servidor que esteja rodando e esperando conex�o na porta 10000.

1.3 - gui.WindowListFriends
	Aqui temos a lista dos usu�rios que s�o amigos do usu�rio que est� logado no cliente. A lista exibe todos os contatos e informa quais est�o 
	online e quais est�o offline. S� � poss�vel conversar com os que est�o online, clicando na lista. Algumas vezes h� uma certa demora em atulizar o status,
	por�m isto � devido ao m�todo de atualiza��o do componente que usamos (JList) da classe Swing n�o ser thread-safe, e portanto h� necessidade da thread que
	atualiza esperar em uma fila. Contudo, sempre em menos de (30 segundos)
	 
1.4 - gui.WindowTalk
	Esta tela implementa a conversa, com o usu�rio digitando as mensagens que deseja enviar para o outro e exibindo todas as mensagens da conversa 
	(recebidas e enviadas). Caso ocorra um erro pelo usu�rio estar offline e a lista ainda n�o estar atualizada, a mensagem n�o � enviada.

1.5 - socket.client.KeepAlive
	Aqui temos a thread do cliente que fica enviando mensagens de se o cliente est� no ar. Enviamos a cada 0,5 segundo uma requisi��o, de maneira que o 
	cliente	n�o fique por muito tempo desatualizado com o status dos amigos. A cada requisi��o abrimos uma nova conex�o, para n�o deixar a conex�o aberta 
	por muito tempo, j� que o servidor s� atualiza o status como offline depois de n�o receber 10 mensagens de keep alive (5 segundos),e manter muitas
	conex�es simult�neas pode acarretar em perda de performance.

1.6 - socket.client.ReceiveMsg
	Esta � a thread respons�vel por receber todas as mensagens enviadas pelos outros peers. Ela possui uma lista com todas as janelas de chat abertas e
	redireciona para a janela correta. Caso n�o houver janela entre estes peers, esta thread abre uma nova janela de conversa.

1.7 - socket.PutUserInactive
	Esta � a thread do servidor que verifica h� quanto tempo os clientes n�o enviaram uma mensagem de keep alive, removendo-o da lista de usu�rios online

1.8 - socket.server.RequestType
	Esta classe � s� um enumerador para cada tipo de mensagem, somente para abstrair parte do protocolo. 
	
1.9 - socket.server.Server
	Este � o servidor, respons�vel por enviar para os usu�rios e amigos em qual porta escutar/enviar as mensagens, receber as conex�es dos usu�rios e enviar
	os amigos online/offline de cada peer.
	
4 - Observa��es
	- A atuliza��o da lista pode demorar alguns segundos (cerca de menos de 15 segundos) devido ao m�todo utilizado da classe Swing n�o ser thread-safe
	- No envio das mensagens, favor n�o pressionar o enter, pois ficar� com uma linha vazia e sua mensagem (antes do enter) n�o ser� enviada para o outro
	peer, pois o programa interpreta que � uma linha vazia, j� que pega somente  �ltima linha (podendo acarretar em outros problemas).
	- Acontece problemas quando h� mais de um servidor rodando no mesmo ip, j� que a porta � fixa, sendo lan�ada uma exce��o por duas aplica��es escutando 
	na mesma porta
	- Rodar o .bat do servidor e o jar da aplica��o no diret�rio que foi enviado, j� que pode acarretar em problemas caso tentar rodar fora, principalmente
	o .bat que compila os arquivos antes de rodar.
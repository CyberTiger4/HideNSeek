<h1>HideAndSeek – Plugin Minecraft per Eventi</h1>

<p><strong>Versione:</strong> 1.0<br>
<strong>API richiesta:</strong> Spigot/Paper 1.20+</p>

<p>Plugin professionale per organizzare partite di <strong>Hide & Seek</strong> in server grandi ed eventi, con countdown hype, lobby personalizzabile e meccaniche avanzate per seekers e hider.</p>

<hr>

<h2>Funzionalità principali</h2>

<h3>Partita</h3>
<ul>
<li>Countdown pre-partita personalizzabile in chat e title (verde → arancione → rosso).</li>
<li>Minimo giocatori configurabile (<code>min-players</code>) prima di avviare la partita.</li>
<li>Possibilità di scegliere manualmente uno o più <strong>seekers</strong> o farli selezionare casualmente.</li>
<li>Giocatori teletrasportati subito all’inizio:
  <ul>
    <li><strong>Hider</strong> randomicamente nella mappa, fuori dal raggio della lobby.</li>
    <li><strong>Seeker</strong> nella lobby, con <strong>cecità e immobilità</strong> per un tempo configurabile (<code>seeker-blind-time</code>).</li>
  </ul>
</li>
</ul>

<h3>Lobby di attesa</h3>
<ul>
<li>Posizione configurabile via comando <code>/hssetlobby [x y z yaw pitch]</code> oppure prende la posizione del giocatore.</li>
<li>Raggio della lobby configurabile (<code>lobby.radius</code>) per evitare spawn troppo vicini.</li>
<li>Teletrasporto automatico dei seeker e hider durante l’inizio della partita.</li>
</ul>

<h3>Meccaniche di gioco</h3>
<ul>
<li><strong>Seeker Stick</strong>: unico strumento per i seeker, necessario per colpire gli hider.</li>
<li>1 colpo con lo stick → l’hider diventa spettatore e riceve messaggio “Sei morto”.</li>
<li>Timer di partita configurabile (<code>seeker-time</code>).</li>
<li>Fine partita automatica:
  <ul>
    <li>Tutti gli hider eliminati → vittoria seekers.</li>
    <li>Scade il timer → vittoria hider.</li>
  </ul>
</li>
<li>Messaggi e broadcast chiari per vittoria.</li>
</ul>

<h3>Effetti e visivi</h3>
<ul>
<li>Countdown hype:
  <ul>
    <li>&gt;10 secondi → chat countdown</li>
    <li>10–6 secondi → title verde</li>
    <li>5–4 secondi → title arancione</li>
    <li>3–1 secondi → title rosso</li>
  </ul>
</li>
<li>Seeker iniziali ciechi e immobilizzati per la durata configurata, con effetti <code>Blindness</code> e <code>Slowness</code>.</li>
</ul>

<hr>

<h2>Comandi</h2>

<table>
<thead>
<tr><th>Comando</th><th>Descrizione</th><th>Permessi</th></tr>
</thead>
<tbody>
<tr>
<td><code>/hsstart [seeker1 seeker2 ...]</code></td>
<td>Avvia una partita. Se non specificati, i seeker vengono scelti casualmente. Avvia il countdown pre-partita.</td>
<td>OP</td>
</tr>
<tr>
<td><code>/hssetlobby [x y z yaw pitch]</code></td>
<td>Setta la lobby di attesa. Se non forniti i parametri, prende la posizione del giocatore.</td>
<td>OP</td>
</tr>
</tbody>
</table>

<p><strong>Tab complete:</strong> <code>/hsstart</code> suggerisce automaticamente i nomi dei giocatori online.</p>

<hr>

<h2>Configurazione (<code>config.yml</code>)</h2>

<pre><code>seeker-time: 120           # Secondi totali della partita
min-players: 4             # Minimo giocatori per far partire il countdown
pre-start-chat: true        # Mostra countdown pre-partita in chat
pre-start-time: 15          # Secondi del countdown pre-partita

lobby:
  world: world
  x: 0.0
  y: 64.0
  z: 0.0
  yaw: 0.0
  pitch: 0.0
  radius: 20.0             # Raggio della lobby per evitare spawn troppo vicino

seeker-blind-time: 60       # Secondi di cecità e immobilità iniziale per i seeker
</code></pre>

<p>Tutti i valori sono modificabili a piacere per adattarsi al server e alla mappa.</p>

<hr>

<h2>Meccaniche avanzate</h2>
<ul>
<li><strong>Spawn sicuro hider:</strong> generato random fuori dal raggio della lobby per garantire equilibrio.</li>
<li><strong>Cooldown e immobilità seeker:</strong> impedisce che i seeker inizino subito a muoversi, aumentando suspense e hype.</li>
<li><strong>Messaggi personalizzati e chiari:</strong> broadcast e title rendono l’esperienza immersiva.</li>
</ul>

<hr>

<h2>Dipendenze</h2>
<ul>
<li>Spigot o Paper 1.21+</li>
<li>Nessun’altra dipendenza richiesta</li>
</ul>

<hr>

<h2>Installazione</h2>
<ol>
<li>Mettere il <code>.jar</code> del plugin nella cartella <code>plugins</code> del server.</li>
<li>Avviare il server per generare il <code>config.yml</code>.</li>
<li>Configurare la lobby e i parametri secondo le preferenze.</li>
<li>Usare <code>/hssetlobby</code> per settare la posizione della lobby.</li>
<li>Usare <code>/hsstart</code> per avviare la partita.</li>
</ol>

<hr>

<h2>Consigli per eventi</h2>
<ul>
<li>Configura il countdown pre-partita tra 15 e 30 secondi per hype.</li>
<li>Usa mappe grandi e raggio della lobby minimo di 20–30 blocchi.</li>
<li>Limitare il numero di seeker a 1–3 per partite grandi.</li>
<li>Considerare effetti extra per suspense (suoni, particelle, ecc.).</li>
</ul>
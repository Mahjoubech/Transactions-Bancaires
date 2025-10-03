package dao;

import entity.Compte;
import entity.CompteCourant;
import entity.CompteEpargne;
import enums.typeCompte;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompteRepoImp implements CompteRepository {
    private static Connection conn;

    public CompteRepoImp(Connection connection) {
        this.conn = connection;
    }

    @Override
    public void create(Compte compte) {
        String request = "INSERT INTO compte (id, numero, idClient, solde, typeCompte, decouvertAutorise, tauxInteret) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (var ps = conn.prepareStatement(request)) {
            ps.setString(1, compte.getId());
            ps.setString(2, compte.getNum());
            ps.setString(3, compte.getIdClient());
            ps.setDouble(4, compte.getSolde());
            ps.setString(5, compte.getTypeCompte().name());

            if (compte instanceof CompteCourant cc) {
                ps.setDouble(6, cc.getDecouvert());
                ps.setDouble(7, 0.0);
            } else if (compte instanceof CompteEpargne ce) {
                ps.setDouble(6, 0.0);
                ps.setDouble(7, ce.getTauxInteret());
            } else {
                ps.setDouble(6, 0.0);
                ps.setDouble(7, 0.0);
            }

            ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(Compte compte) {
        String request = "UPDATE compte SET numero = ?, idClient = ?, solde = ?, typeCompte = ?, decouvertAutorise = ?, tauxInteret = ? WHERE id = ?";
        try (var ps = conn.prepareStatement(request)) {
            ps.setString(1, compte.getNum());
            ps.setString(2, compte.getIdClient());
            ps.setDouble(3, compte.getSolde());
            ps.setString(4, compte.getTypeCompte().name());

            if (compte instanceof CompteCourant cc) {
                ps.setDouble(5, cc.getDecouvert());
                ps.setDouble(6, 0.0);
            } else if (compte instanceof CompteEpargne ce) {
                ps.setDouble(5, 0.0);
                ps.setDouble(6, ce.getTauxInteret());
            } else {
                ps.setDouble(5, 0.0);
                ps.setDouble(6, 0.0);
            }

            ps.setString(7, compte.getId());
            ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(String id) {
        String request = "DELETE FROM compte WHERE id = ?";
        try (var ps = conn.prepareStatement(request)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<Compte> findById(String id) {
        String request = "SELECT * FROM compte WHERE id = ?";
        try (var ps = conn.prepareStatement(request)) {
            ps.setString(1, id);
            var rs = ps.executeQuery();
            if (rs.next()) {
                String num = rs.getString("numero");
                String idClient = rs.getString("idClient");
                double solde = rs.getDouble("solde");
                String typ = rs.getString("typeCompte");

                if ("COURANT".equals(typ)) {
                    double decouvert = rs.getDouble("decouvertAutorise");
                    return Optional.of(new CompteCourant(id, num, idClient, solde, typeCompte.COURANT, decouvert));
                } else if ("EPARGNE".equals(typ)) {
                    double taux = rs.getDouble("tauxInteret");
                    return Optional.of(new CompteEpargne(id, num, idClient, solde, typeCompte.EPARGNE, taux));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return Optional.empty();
    }

    @Override
    public List<Compte> findAll() {
        String request = "SELECT * FROM compte";
        try (var ps = conn.prepareStatement(request)) {
            var rs = ps.executeQuery();
            List<Compte> comptes = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("id");
                String num = rs.getString("numero");
                String idClient = rs.getString("idClient");
                double solde = rs.getDouble("solde");
                String typd = rs.getString("typeCompte");

                if ("COURANT".equals(typd)) {
                    double decouvert = rs.getDouble("decouvertAutorise");
                    comptes.add(new CompteCourant(id, num, idClient, solde, typeCompte.COURANT, decouvert));
                } else if ("EPARGNE".equals(typd)) {
                    double taux = rs.getDouble("tauxInteret");
                    comptes.add(new CompteCourant(id, num, idClient, solde, typeCompte.EPARGNE, taux));
                }
            }
            return comptes;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Compte> findByClientEmail(String email) {
        String request = "SELECT c.* FROM compte c JOIN client cl ON c.idClient = cl.id WHERE cl.email = ?";
        try (var ps = conn.prepareStatement(request)) {
            ps.setString(1, email);
            var rs = ps.executeQuery();
            List<Compte> comptes = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("id");
                String num = rs.getString("numero");
                String idClient = rs.getString("idClient");
                double solde = rs.getDouble("solde");
                String typ = rs.getString("typeCompte");

                if ("COURANT".equals(typ)) {
                    double decouvert = rs.getDouble("decouvertAutorise");
                    comptes.add(new CompteCourant(id, num, idClient, solde, typeCompte.COURANT, decouvert ));
                } else if ("EPARGNE".equals(typ)) {
                    double taux = rs.getDouble("tauxInteret");
                    comptes.add(new CompteEpargne(id, num, idClient, solde, typeCompte.EPARGNE, taux ) );
                }
            }
            return comptes;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Compte> findByClientNom(String nom) {
        String request = "SELECT c.* FROM compte c JOIN client cl ON c.idClient = cl.id WHERE cl.nom = ?";
        try (var ps = conn.prepareStatement(request)) {
            ps.setString(1, nom);
            var rs = ps.executeQuery();
            List<Compte> comptes = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("id");
                String num = rs.getString("numero");
                String idClient = rs.getString("idClient");
                double solde = rs.getDouble("solde");
                String typ = rs.getString("typeCompte");

                if ("COURANT".equals(typ)) {
                    double decouvert = rs.getDouble("decouvertAutorise");
                    comptes.add(new CompteCourant(id, num, idClient, solde, typeCompte.COURANT, decouvert));
                } else if ("EPARGNE".equals(typ)) {
                    double taux = rs.getDouble("tauxInteret");
                    comptes.add(new CompteEpargne(id, num, idClient, solde, typeCompte.EPARGNE, taux));
                }
            }
            return comptes;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Compte> findByClientid(String idClient) {
        String request = "SELECT * FROM compte WHERE idClient = ?";
        try (var ps = conn.prepareStatement(request)) {
            ps.setString(1, idClient);
            var rs = ps.executeQuery();
            List<Compte> comptes = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("id");
                String num = rs.getString("numero");
                double solde = rs.getDouble("solde");
                String typ = rs.getString("typeCompte");

                if ("COURANT".equals(typ)) {
                    double decouvert = rs.getDouble("decouvertAutorise");
                    comptes.add(new CompteCourant(id, num, idClient, solde, typeCompte.COURANT, decouvert));
                } else if ("EPARGNE".equals(typ)) {
                    double taux = rs.getDouble("tauxInteret");
                    comptes.add(new CompteEpargne(id, num, idClient, solde, typeCompte.EPARGNE, taux));
                }
            }
            return comptes;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public double getTotalSoldeByClient(String clientId) {
        String request = "SELECT SUM(solde) FROM compte WHERE idClient = ?";
        try (var ps = conn.prepareStatement(request)) {
            ps.setString(1, clientId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return 0;
    }
    @Override
    public int countByClient(String clientId) {
        String request = "SELECT COUNT(*) FROM compte WHERE idClient = ?";
        try (var ps = conn.prepareStatement(request)) {
            ps.setString(1, clientId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return 0;
    }
}
